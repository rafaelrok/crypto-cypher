package br.com.rafaelvieira.cryptocypher;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration(proxyBeanMethods = false)
public class CryptoCypherApplication {
    public static void main(String[] args) {
        Application.launch(AppCryptoCypher.class, args);
    }
}
