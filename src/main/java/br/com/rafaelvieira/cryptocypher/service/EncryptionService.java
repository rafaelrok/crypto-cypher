package br.com.rafaelvieira.cryptocypher.service;

import br.com.rafaelvieira.cryptocypher.enums.CryptographyType;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


@Service
public class EncryptionService {

    private CryptographyStrategy strategy;

    public EncryptionService() {
        setCryptographyType(CryptographyType.AES);
    }

    public void setCryptographyType(CryptographyType type) {
        this.strategy = type.createStrategy();
    }

    public void exportOutput(File file, String content) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            throw new IOException("Erro ao exportar o arquivo: " + file.getPath(), e);
        }
    }

    public String encrypt(String input) throws Exception {
        validateInput(input);
        return strategy.encrypt(input);
    }

    public String decrypt(String input) throws Exception {
        validateInput(input);
        return strategy.decrypt(input);
    }

    private void validateInput(String input) {
        if (strategy == null) {
            throw new IllegalStateException("Estratégia de criptografia não inicializada");
        }
    }
}
