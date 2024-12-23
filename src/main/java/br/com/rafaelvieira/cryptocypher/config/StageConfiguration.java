package br.com.rafaelvieira.cryptocypher.config;

import javafx.stage.Stage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration
public class StageConfiguration {

    @Bean
    @Lazy
    @Scope("singleton")
    public Stage primaryStage() {
        throw new IllegalStateException(
                "Stage must be created by JavaFX runtime");
    }
}
