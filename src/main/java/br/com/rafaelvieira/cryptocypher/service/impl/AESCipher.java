package br.com.rafaelvieira.cryptocypher.service.impl;

import br.com.rafaelvieira.cryptocypher.service.CryptographyStrategy;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESCipher implements CryptographyStrategy {
    private static SecretKey key;
    private static final String ALGORITHM = "AES";

    public AESCipher() {
        try {
            // Gera uma chave AES de 256 bits
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(256);
            key = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao inicializar AES", e);
        }
    }

    @Override
    public String encrypt(String input) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erro na criptografia AES", e);
        }
    }

    @Override
    public String decrypt(String input) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(input));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erro na descriptografia AES", e);
        }
    }

    // Método para obter a chave atual (útil para armazenamento/recuperação)
    public static String getKeyAsString() {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // Método para definir uma chave existente
    public static void setKeyFromString(String keyStr) {
        byte[] decodedKey = Base64.getDecoder().decode(keyStr);
        key = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }
}

