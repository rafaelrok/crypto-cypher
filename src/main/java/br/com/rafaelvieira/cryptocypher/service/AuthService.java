package br.com.rafaelvieira.cryptocypher.service;

import br.com.rafaelvieira.cryptocypher.model.role.Role;
import br.com.rafaelvieira.cryptocypher.model.user.User;
import br.com.rafaelvieira.cryptocypher.enums.ERole;
import br.com.rafaelvieira.cryptocypher.payload.request.UserAuthentication;
import br.com.rafaelvieira.cryptocypher.payload.request.UserRegister;
import br.com.rafaelvieira.cryptocypher.payload.response.JwtResponse;
import br.com.rafaelvieira.cryptocypher.repository.RoleRepository;
import br.com.rafaelvieira.cryptocypher.repository.UserRepository;
import br.com.rafaelvieira.cryptocypher.security.UserDetailsImpl;
import br.com.rafaelvieira.cryptocypher.security.jwt.JwtUtils;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final VerificationService verificationService;
    private final EmailService emailService;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       VerificationService verificationService,
                       EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.verificationService = verificationService;
        this.emailService = emailService;
    }

    @Transactional
    public JwtResponse authenticateUser(UserAuthentication userAuthentication) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userAuthentication.getUsername(), userAuthentication.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Gera o cookie JWT
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) {
            throw new RuntimeException("Please verify your email first");
        }

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken(jwtCookie.getValue());
        jwtResponse.setTokenCookie(jwtCookie);
        jwtResponse.setId(userDetails.getId());
        jwtResponse.setUsername(userDetails.getUsername());
        jwtResponse.setEmail(userDetails.getEmail());
        jwtResponse.setRoles(roles);
        jwtResponse.setFirstAccess(user.isFirstAccess());
        return jwtResponse;

//        return JwtResponse.builder()
//                .token(jwtCookie.getValue()) // Pegamos o valor do cookie
//                .tokenCookie(jwtCookie)      // Adicionamos o cookie completo
//                .id(userDetails.getId())
//                .username(userDetails.getUsername())
//                .email(userDetails.getEmail())
//                .roles(roles)
//                .isFirstAccess(user.isFirstAccess())
//                .build();
    }

    @Transactional
    public void registerUser(UserRegister userRegister) throws MessagingException {
        if (userRepository.existsByUsername(userRegister.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        if (userRepository.existsByEmail(userRegister.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create verification code
        String verificationCode = verificationService.generateVerificationCode();

        // Create new user
        User user = new User(
                userRegister.getUsername(),
                userRegister.getEmail(),
                passwordEncoder.encode(userRegister.getPassword()),
                false,
                true,
                verificationCode,
                getRoles(userRegister.getRoles())
        );

        userRepository.save(user);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), verificationCode);
    }

    @Transactional
    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

    private Set<Role> getRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role MODERATOR is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
                        roles.add(userRole);
                }
            });
        }

        return roles;
    }

    public boolean validateToken(String token) {
        return jwtUtils.validateJwtToken(token);
    }

    public String getUsernameFromToken(String token) {
        return jwtUtils.getUserNameFromJwtToken(token);
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Transactional
    public void verifyEmailAndCompleteRegistration(String email, String code) {
        if (verificationService.verifyUser(email, code)) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setVerified(true);
            user.setVerificationCode(null);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Invalid verification code");
        }
    }
}
