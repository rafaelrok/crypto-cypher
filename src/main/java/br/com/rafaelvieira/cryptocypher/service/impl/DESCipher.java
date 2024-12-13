package br.com.rafaelvieira.cryptocypher.service.impl;

import br.com.rafaelvieira.cryptocypher.service.CryptographyStrategy;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DESCipher implements CryptographyStrategy {
    private static SecretKey key;
    private static final String ALGORITHM = "DES";

    public DESCipher() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(56); // DES usa chaves de 56 bits
            key = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao inicializar DES", e);
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
            throw new RuntimeException("Erro na criptografia DES", e);
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
            throw new RuntimeException("Erro na descriptografia DES", e);
        }
    }
}
