package br.com.rafaelvieira.cryptocypher.service.impl;

import br.com.rafaelvieira.cryptocypher.service.CryptographyStrategy;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

public class RSACipher implements CryptographyStrategy {
    private static PrivateKey privateKey;
    private static PublicKey publicKey;
    private static final String ALGORITHM = "RSA";

    public RSACipher() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(2048); // Tamanho da chave RSA
            KeyPair pair = keyGen.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao inicializar RSA", e);
        }
    }

    @Override
    public String encrypt(String input) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erro na criptografia RSA", e);
        }
    }

    @Override
    public String decrypt(String input) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(input));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erro na descriptografia RSA", e);
        }
    }

    // Métodos para obter as chaves (útil para armazenamento/recuperação)
    public static String getPublicKeyAsString() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public static String getPrivateKeyAsString() {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }
}
