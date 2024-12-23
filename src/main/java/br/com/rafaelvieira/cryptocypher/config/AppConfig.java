package br.com.rafaelvieira.cryptocypher.config;

import br.com.rafaelvieira.cryptocypher.logging.ExceptionWriter;
import br.com.rafaelvieira.cryptocypher.service.AuthService;
import br.com.rafaelvieira.cryptocypher.service.EncryptionService;
import br.com.rafaelvieira.cryptocypher.service.NavigationService;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.io.StringWriter;
import java.util.ResourceBundle;

@Configuration
public class AppConfig {

    @Autowired
    SpringFXMLLoader springFXMLLoader;

    @Autowired
    private AuthService authService;

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
