package br.com.rafaelvieira.cryptocypher;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(proxyBeanMethods = false)
public class CryptoCypherApplication {

    public static void main(String[] args) {
        Application.launch(AppCryptoCypher.class, args);
    }
}
