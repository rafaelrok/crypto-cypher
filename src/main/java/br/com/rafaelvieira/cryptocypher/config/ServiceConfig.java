package br.com.rafaelvieira.cryptocypher.config;

import br.com.rafaelvieira.cryptocypher.repository.RoleRepository;
import br.com.rafaelvieira.cryptocypher.repository.UserRepository;
import br.com.rafaelvieira.cryptocypher.service.AuthService;
import br.com.rafaelvieira.cryptocypher.service.EmailService;
import br.com.rafaelvieira.cryptocypher.service.VerificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan(basePackages = {
        "br.com.rafaelvieira.cryptocypher.service",
        "br.com.rafaelvieira.cryptocypher.controller"
})
public class ServiceConfig {

    @Bean
    public AuthService authService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            VerificationService verificationService,
            EmailService emailService) {
        return new AuthService(
                authenticationManager,
                userRepository,
                roleRepository,
                passwordEncoder,
                verificationService,
                emailService
        );
    }

    // Outros beans necessários...
}
