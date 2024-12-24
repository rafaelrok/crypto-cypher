package br.com.rafaelvieira.cryptocypher.service;

import br.com.rafaelvieira.cryptocypher.model.role.Role;
import br.com.rafaelvieira.cryptocypher.model.user.User;
import br.com.rafaelvieira.cryptocypher.enums.ERole;
import br.com.rafaelvieira.cryptocypher.payload.request.UserRegister;
import br.com.rafaelvieira.cryptocypher.repository.RoleRepository;
import br.com.rafaelvieira.cryptocypher.repository.UserRepository;
import br.com.rafaelvieira.cryptocypher.security.UserDetailsImpl;
import br.com.rafaelvieira.cryptocypher.util.CodeGeneratorService;
import br.com.rafaelvieira.cryptocypher.util.FxTypeName;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private VerificationService verificationService;
    private EmailService emailService;

    // Objeto para armazenar o usuário autenticado na sessão
    private UserSession currentSession;

    public AuthService() {
    }

    @PostConstruct
    public void init() {
        log.info("AuthService inicializado");
    }

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       VerificationService verificationService,
                       EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationService = verificationService;
        this.emailService = emailService;
    }

    @FxTypeName("user")
    public static class UserSession {
        private Long id;
        private String username;
        private String email;
        private String verificationCode;
        private List<String> roles;
        private boolean firstAccess;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getVerificationCode() {
            return verificationCode;
        }

        public void setVerificationCode(String verificationCode) {
            this.verificationCode = verificationCode;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public boolean isFirstAccess() {
            return firstAccess;
        }

        public void setFirstAccess(boolean firstAccess) {
            this.firstAccess = firstAccess;
        }
    }

    public boolean hasAnyUsers() {
        try {
            long count = userRepository.count();
            log.debug("Number of users in system: {}", count);
            return count > 0;
        } catch (Exception e) {
            log.error("Error checking for users", e);
            return false;
        }
    }

    public UserSession authenticateUser(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            final CodeGeneratorService codeGeneratorService = new CodeGeneratorService();
            final String code = codeGeneratorService.generateVerificationCode();

            // Criar e armazenar a sessão do usuário
            currentSession = new UserSession();
            currentSession.setId(userDetails.getId());
            currentSession.setUsername(userDetails.getUsername());
            currentSession.setEmail(userDetails.getEmail());
            currentSession.setVerificationCode(code);
            currentSession.setRoles(roles);
            currentSession.setFirstAccess(user.isFirstAccess());

            return currentSession;

        } catch (DisabledException e) {
            throw new RuntimeException("Por favor, verifique seu email primeiro");
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Senha inválida");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Usuário não encontrado");
        }
    }

    public void logout() {
        SecurityContextHolder.clearContext();
        currentSession = null;
    }

    @Transactional
    public void registerUser(UserRegister userRegister) throws MessagingException {
        if (userRepository.existsByUsername(userRegister.getUsername())) {
            throw new RuntimeException("Username já está em uso");
        }

        if (userRepository.existsByEmail(userRegister.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        User user = new User();
        user.setUsername(userRegister.getUsername());
        user.setFullName(userRegister.getFullName());
        user.setEmail(userRegister.getEmail());
        user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        user.setVerified(false);
        user.setFirstAccess(true);

        // Adicionar role padrão
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));
        user.setRoles(Collections.singleton(userRole));

        // Gerar e enviar código de verificação
        final CodeGeneratorService codeGeneratorService = new CodeGeneratorService();
        String verificationCode = codeGeneratorService.generateVerificationCode();
        user.setVerificationCode(verificationCode);
        userRepository.save(user);

        emailService.sendVerificationEmail(userRegister.getEmail(), verificationCode);
    }

    public void verifyEmail(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (user.isVerified()) {
            throw new RuntimeException("Email já foi verificado");
        }

        if (!user.getVerificationCode().equals(code)) {
            throw new RuntimeException("Código de verificação inválido");
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        userRepository.save(user);
    }

    public void requestPasswordReset(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        final CodeGeneratorService codeGeneratorService = new CodeGeneratorService();
        String resetCode = codeGeneratorService.generateVerificationCode();
        user.setVerificationCode(resetCode);
        userRepository.save(user);

        emailService.sendPasswordResetEmail(email, resetCode);
    }

    public void resetPassword(String email, String code, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!user.getPassword().equals(code)) {
            throw new RuntimeException("Código de reset inválido");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setVerificationCode(null);
        userRepository.save(user);
    }

    @Transactional
    public void initiatePasswordReset(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        final CodeGeneratorService codeGeneratorService = new CodeGeneratorService();
        String resetCode = codeGeneratorService.generateVerificationCode();
        user.setVerificationCode(resetCode);
        userRepository.save(user);

        emailService.sendPasswordResetEmail(email, resetCode);
    }

    public UserSession getCurrentSession() {
        return currentSession;
    }

    public boolean isAuthenticated() {
        return currentSession != null;
    }
}
