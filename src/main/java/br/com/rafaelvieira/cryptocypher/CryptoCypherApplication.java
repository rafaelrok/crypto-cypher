package br.com.rafaelvieira.cryptocypher;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(proxyBeanMethods = false)
@EntityScan("br.com.rafaelvieira.cryptocypher.model")
@EnableJpaRepositories("br.com.rafaelvieira.cryptocypher.repository")
@ComponentScan(basePackages = {
        "br.com.rafaelvieira.cryptocypher",
        "br.com.rafaelvieira.cryptocypher.service",
        "br.com.rafaelvieira.cryptocypher.config",
        "br.com.rafaelvieira.cryptocypher.controller",
        "br.com.rafaelvieira.cryptocypher.logging",
        "br.com.rafaelvieira.cryptocypher.model",
        "br.com.rafaelvieira.cryptocypher.repository",
        "br.com.rafaelvieira.cryptocypher.service",
        "br.com.rafaelvieira.cryptocypher.view",
        "br.com.rafaelvieira.cryptocypher.util"
})
public class CryptoCypherApplication {

    public static void main(String[] args) {
        Application.launch(AppCryptoCypher.class, args);
    }
}
