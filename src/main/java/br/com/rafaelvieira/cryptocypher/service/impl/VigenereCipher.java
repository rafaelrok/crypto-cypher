package br.com.rafaelvieira.cryptocypher.service.impl;

import br.com.rafaelvieira.cryptocypher.service.CryptographyStrategy;

public class VigenereCipher implements CryptographyStrategy {
    private final String key;

    public VigenereCipher(String key) {
        this.key = key.toUpperCase();
    }

    @Override
    public String encrypt(String input) {
        StringBuilder encrypted = new StringBuilder();
        input = input.toUpperCase();
        int keyLength = key.length();

        for (int i = 0; i < input.length(); i++) {
            char inputChar = input.charAt(i);
            if (Character.isLetter(inputChar)) {
                int keyChar = key.charAt(i % keyLength) - 'A';
                char encryptedChar = (char) (((inputChar - 'A' + keyChar) % 26) + 'A');
                encrypted.append(encryptedChar);
            } else {
                encrypted.append(inputChar);
            }
        }
        return encrypted.toString();
    }

    @Override
    public String decrypt(String input) {
        StringBuilder decrypted = new StringBuilder();
        input = input.toUpperCase();
        int keyLength = key.length();

        for (int i = 0; i < input.length(); i++) {
            char inputChar = input.charAt(i);
            if (Character.isLetter(inputChar)) {
                int keyChar = key.charAt(i % keyLength) - 'A';
                char decryptedChar = (char) (((inputChar - 'A' - keyChar + 26) % 26) + 'A');
                decrypted.append(decryptedChar);
            } else {
                decrypted.append(inputChar);
            }
        }
        return decrypted.toString();
    }
}
