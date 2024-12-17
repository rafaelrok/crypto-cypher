package br.com.rafaelvieira.cryptocypher.service.impl;

import br.com.rafaelvieira.cryptocypher.service.CryptographyStrategy;

public class VigenereCipher implements CryptographyStrategy {
    private final String key;

    public VigenereCipher(String key) {
        this.key = key;
    }

    @Override
    public String encrypt(String input) {
        StringBuilder encrypted = new StringBuilder();
        input = input.toUpperCase();
        int keyLength = key.length();

        for (int i = 0; i < input.length(); i++) {
            char inputChar = input.charAt(i);
            if (Character.isLetter(inputChar)) {
                int keyChar = key.charAt(i % keyLength) - 'a';
                char encryptedChar = (char) (((inputChar - 'a' + keyChar) % 26) + 'a');
                encrypted.append(encryptedChar);
            } else {
                encrypted.append(inputChar);
            }
        }
        return encrypted.toString();
    }

    @Override
    public String decrypt(String input) {
        String[] lines = input.split("\n");
        StringBuilder decrypted = new StringBuilder();

        for (String line : lines) {
            StringBuilder decryptedLine = new StringBuilder();
            line = line.toUpperCase();
            int keyLength = key.length();

            for (int i = 0; i < line.length(); i++) {
                char inputChar = line.charAt(i);
                if (Character.isLetter(inputChar)) {
                    int keyChar = key.charAt(i % keyLength) - 'a';
                    char decryptedChar = (char) (((inputChar - 'a' - keyChar + 26) % 26) + 'a');
                    decryptedLine.append(decryptedChar);
                } else {
                    decryptedLine.append(inputChar);
                }
            }
            decrypted.append(decryptedLine).append("\n");
        }

        return decrypted.toString();
    }
}
