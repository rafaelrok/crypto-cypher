package br.com.rafaelvieira.cryptocypher.config;

import br.com.rafaelvieira.cryptocypher.enums.CryptographyType;
import br.com.rafaelvieira.cryptocypher.service.EncryptionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public EncryptionService encryptionService() {
        return new EncryptionService(CryptographyType.AES);
    }
}
