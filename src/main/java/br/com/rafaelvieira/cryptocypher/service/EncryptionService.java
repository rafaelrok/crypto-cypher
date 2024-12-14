package br.com.rafaelvieira.cryptocypher.service;

import br.com.rafaelvieira.cryptocypher.domain.EncryptionResult;
import br.com.rafaelvieira.cryptocypher.enums.CryptographyType;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@Service
public class EncryptionService {

    private CryptographyType cryptographyType;

    public EncryptionService(CryptographyType cryptographyType) {
        this.cryptographyType = cryptographyType;
    }

    public void setCryptographyType(CryptographyType cryptographyType) {
        this.cryptographyType = cryptographyType;
    }

    public EncryptionResult encryptFile(String filePath) throws IOException {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            String encryptedContent = cryptographyType.encrypt(content);
            return new EncryptionResult(encryptedContent, filePath);
        } catch (IOException e) {
            throw new IOException("Erro ao ler ou criptografar o arquivo: " + filePath, e);
        }
    }

    public EncryptionResult decryptFile(String filePath) throws IOException {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            String decryptedContent = cryptographyType.decrypt(content);
            return new EncryptionResult(decryptedContent, filePath);
        } catch (IOException e) {
            throw new IOException("Erro ao ler ou descriptografar o arquivo: " + filePath, e);
        }
    }

    public void exportOutput(File file, String content) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            throw new IOException("Erro ao exportar o arquivo: " + file.getPath(), e);
        }
    }

    // Métodos de conveniência para criptografar/descriptografar strings diretamente
    public String encrypt(String input) {
        return cryptographyType.encrypt(input);
    }

    public String decrypt(String input) {
        return cryptographyType.decrypt(input);
    }

//    private static final int SHIFT = 3; // Número de posições para deslocar na cifra de César
//
//    public EncryptionResult encryptFile(String filePath) throws IOException {
//        String content = new String(Files.readAllBytes(Paths.get(filePath)));
//        String encryptedContent = encrypt(content);
//        return new EncryptionResult(encryptedContent, filePath);
//    }
//
//    public EncryptionResult decryptFile(String filePath) throws IOException {
//        String content = new String(Files.readAllBytes(Paths.get(filePath)));
//        String decryptedContent = decrypt(content);
//        return new EncryptionResult(decryptedContent, filePath);
//    }
//
//    public void exportOutput(File file, String content) throws IOException {
//        try (FileWriter writer = new FileWriter(file)) {
//            writer.write(content);
//        }
//    }
//
//    public String encrypt(String input) {
//        StringBuilder encrypted = new StringBuilder();
//        for (char c : input.toCharArray()) {
//            if (Character.isLetter(c)) {
//                char base = Character.isLowerCase(c) ? 'a' : 'A';
//                // Desloca SHIFT posições para frente
//                c = (char) ((c - base + SHIFT) % 26 + base);
//            }
//            encrypted.append(c);
//        }
//        return encrypted.toString();
//    }
//
//    public String decrypt(String input) {
//        StringBuilder decrypted = new StringBuilder();
//        for (char c : input.toCharArray()) {
//            if (Character.isLetter(c)) {
//                char base = Character.isLowerCase(c) ? 'a' : 'A';
//                // Para descriptografar, movemos SHIFT posições para trás
//                int shifted = c - base;
//                shifted = (shifted - SHIFT) % 26;
//                // Ajuste para valores negativos
//                if (shifted < 0) {
//                    shifted += 26;
//                }
//                c = (char) (shifted + base);
//            }
//            decrypted.append(c);
//        }
//        return decrypted.toString();
//    }
}
