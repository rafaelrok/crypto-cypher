package br.com.rafaelvieira.cryptocypher.config;

import br.com.rafaelvieira.cryptocypher.logging.ExceptionWriter;
import br.com.rafaelvieira.cryptocypher.repository.RoleRepository;
import br.com.rafaelvieira.cryptocypher.repository.UserRepository;
import br.com.rafaelvieira.cryptocypher.service.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.StringWriter;
import java.util.ResourceBundle;

@Configuration
@ComponentScan(basePackages = "br.com.rafaelvieira.cryptocypher")
public class AppConfig {

    @Autowired
    SpringFXMLLoader springFXMLLoader;

    @Autowired
    private AuthService authService;

    @Bean
    @Lazy
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

    @Bean
    public EncryptionService encryptionService() {
        return new EncryptionService();
    }

    @Bean
    @Scope("prototype")
    public ExceptionWriter exceptionWriter() {
        return new ExceptionWriter(new StringWriter());
    }

    @Bean
    public ResourceBundle resourceBundle() {
        return ResourceBundle.getBundle("Bundle");
    }

    @Bean
    @Lazy
    public NavigationService navigationService() {
        return new NavigationService(authService);
    }

    @Bean
    @Lazy
    public StageManager stageManager(@Qualifier("primaryStage") Stage stage) {
        return new StageManager(springFXMLLoader, stage, navigationService());
    }
}
