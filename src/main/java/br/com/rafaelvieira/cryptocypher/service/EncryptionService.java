package br.com.rafaelvieira.cryptocypher.service;

import br.com.rafaelvieira.cryptocypher.enums.CryptographyType;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


@Service
public class EncryptionService {

    private CryptographyStrategy strategy;
    private CryptographyType currentType;

    public EncryptionService() {
        setCryptographyType(CryptographyType.AES);
    }

    public void setCryptographyType(CryptographyType type) {
        this.currentType = type;
        this.strategy = type.createStrategy();
    }

    public void exportOutput(File file, String content) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            throw new IOException("Erro ao exportar o arquivo: " + file.getPath(), e);
        }
    }

    public String encrypt(String input) {
        try {
            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("Texto para criptografar não pode ser vazio");
            }
            if (strategy == null) {
                throw new IllegalStateException("Estratégia de criptografia não inicializada");
            }
            return strategy.encrypt(input);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar: " + e.getMessage(), e);
        }
    }

    public String decrypt(String input) {
        try {
            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("Texto para descriptografar não pode ser vazio");
            }
            if (strategy == null) {
                throw new IllegalStateException("Estratégia de criptografia não inicializada");
            }
            return strategy.decrypt(input);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Erro ao descriptografar: Formato inválido - " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar: " + e.getMessage(), e);
        }
    }

    //    public EncryptionResult encryptFile(String filePath) throws IOException {
//        try {
//            String content = new String(Files.readAllBytes(Paths.get(filePath)));
//            String encryptedContent = cryptographyType.encrypt(content);
//            return new EncryptionResult(encryptedContent, filePath);
//        } catch (IOException e) {
//            throw new IOException("Erro ao ler ou criptografar o arquivo: " + filePath, e);
//        }
//    }
//
//    public EncryptionResult decryptFile(String filePath) throws IOException {
//        try {
//            String content = new String(Files.readAllBytes(Paths.get(filePath)));
//            String decryptedContent = cryptographyType.decrypt(content);
//            return new EncryptionResult(decryptedContent, filePath);
//        } catch (IOException e) {
//            throw new IOException("Erro ao ler ou descriptografar o arquivo: " + filePath, e);
//        }
//    }
}
