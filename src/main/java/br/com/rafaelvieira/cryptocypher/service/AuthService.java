package br.com.rafaelvieira.cryptocypher.service;

import br.com.rafaelvieira.cryptocypher.model.role.Role;
import br.com.rafaelvieira.cryptocypher.model.user.User;
import br.com.rafaelvieira.cryptocypher.enums.ERole;
import br.com.rafaelvieira.cryptocypher.payload.request.UserRegister;
import br.com.rafaelvieira.cryptocypher.repository.RoleRepository;
import br.com.rafaelvieira.cryptocypher.repository.UserRepository;
import br.com.rafaelvieira.cryptocypher.security.UserDetailsImpl;
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

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;
    private final EmailService emailService;

    // Objeto para armazenar o usuário autenticado na sessão
    private UserSession currentSession;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, VerificationService verificationService, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationService = verificationService;
        this.emailService = emailService;
    }

    public static class UserSession {
        private Long id;
        private String username;
        private String email;
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

            // Criar e armazenar a sessão do usuário
            currentSession = new UserSession();
            currentSession.setId(userDetails.getId());
            currentSession.setUsername(userDetails.getUsername());
            currentSession.setEmail(userDetails.getEmail());
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
        String verificationCode = verificationService.generateVerificationCode();
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

        String resetCode = verificationService.generateVerificationCode();
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

        String resetCode = verificationService.generateVerificationCode();
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

//@Service
//public class AuthService {
//
//    private final AuthenticationManager authenticationManager;
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtUtils jwtUtils;
//    private final VerificationService verificationService;
//    private final EmailService emailService;
//
//    public AuthService(AuthenticationManager authenticationManager,
//                       UserRepository userRepository,
//                       RoleRepository roleRepository,
//                       PasswordEncoder passwordEncoder,
//                       JwtUtils jwtUtils,
//                       VerificationService verificationService,
//                       EmailService emailService) {
//        this.authenticationManager = authenticationManager;
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtUtils = jwtUtils;
//        this.verificationService = verificationService;
//        this.emailService = emailService;
//    }
//
//    @Transactional
//    public JwtResponse authenticateUser(UserAuthentication userAuthentication) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(userAuthentication.getUsername(), userAuthentication.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//        // Gera o cookie JWT
//        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
//
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//
//        User user = userRepository.findByUsername(userDetails.getUsername())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!user.isVerified()) {
//            throw new RuntimeException("Please verify your email first");
//        }
//
//        JwtResponse jwtResponse = new JwtResponse();
//        jwtResponse.setToken(jwtCookie.getValue());
//        jwtResponse.setTokenCookie(jwtCookie);
//        jwtResponse.setId(userDetails.getId());
//        jwtResponse.setUsername(userDetails.getUsername());
//        jwtResponse.setEmail(userDetails.getEmail());
//        jwtResponse.setRoles(roles);
//        jwtResponse.setFirstAccess(user.isFirstAccess());
//        return jwtResponse;
//    }
//
//    @Transactional
//    public void registerUser(UserRegister userRegister) throws MessagingException {
//        if (userRepository.existsByUsername(userRegister.getUsername())) {
//            throw new RuntimeException("Username is already taken!");
//        }
//
//        if (userRepository.existsByEmail(userRegister.getEmail())) {
//            throw new RuntimeException("Email is already in use!");
//        }
//
//        // Create verification code
//        String verificationCode = verificationService.generateVerificationCode();
//
//        // Create new user
//        User user = new User(
//                userRegister.getUsername(),
//                userRegister.getEmail(),
//                passwordEncoder.encode(userRegister.getPassword()),
//                false,
//                true,
//                verificationCode,
//                getRoles(userRegister.getRoles())
//        );
//
//        userRepository.save(user);
//
//        // Send verification email
//        emailService.sendVerificationEmail(user.getEmail(), verificationCode);
//    }
//
//    @Transactional
//    public void resetPassword(String email, String newPassword) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        user.setPassword(passwordEncoder.encode(newPassword));
//        user.setVerificationCode(null);
//        userRepository.save(user);
//    }
//
//    @Transactional
//    public void initiatePasswordReset(String email) throws MessagingException {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String resetCode = verificationService.generateVerificationCode();
//        user.setVerificationCode(resetCode);
//        userRepository.save(user);
//
//        emailService.sendPasswordResetEmail(email, resetCode);
//    }
//
//    private Set<Role> getRoles(Set<String> strRoles) {
//        Set<Role> roles = new HashSet<>();
//
//        if (strRoles == null || strRoles.isEmpty()) {
//            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                    .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
//            roles.add(userRole);
//        } else {
//            strRoles.forEach(role -> {
//                switch (role.toLowerCase()) {
//                    case "admin":
//                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found."));
//                        roles.add(adminRole);
//                        break;
//                    case "mod":
//                        Role modRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Error: Role MODERATOR is not found."));
//                        roles.add(modRole);
//                        break;
//                    default:
//                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                                .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
//                        roles.add(userRole);
//                }
//            });
//        }
//
//        return roles;
//    }
//
//    public boolean validateToken(String token) {
//        return jwtUtils.validateJwtToken(token);
//    }
//
//    public String getUsernameFromToken(String token) {
//        return jwtUtils.getUserNameFromJwtToken(token);
//    }
//
//    public void logout() {
//        SecurityContextHolder.clearContext();
//    }
//
//    @Transactional
//    public void verifyEmailAndCompleteRegistration(String email, String code) {
//        if (verificationService.verifyUser(email, code)) {
//            User user = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//            user.setVerified(true);
//            user.setVerificationCode(null);
//            userRepository.save(user);
//        } else {
//            throw new RuntimeException("Invalid verification code");
//        }
//    }
//}
