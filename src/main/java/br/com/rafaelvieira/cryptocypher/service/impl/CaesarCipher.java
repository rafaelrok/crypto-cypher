package br.com.rafaelvieira.cryptocypher.service.impl;

import br.com.rafaelvieira.cryptocypher.service.CryptographyStrategy;

public class CaesarCipher implements CryptographyStrategy {
    private static final int SHIFT = 3;

    @Override
    public String encrypt(String input) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char) ((c - base + SHIFT) % 26 + base);
            }
            encrypted.append(c);
        }
        return encrypted.toString();
    }

    @Override
    public String decrypt(String input) {
        StringBuilder decrypted = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                int shifted = c - base;
                shifted = (shifted - SHIFT) % 26;
                if (shifted < 0) {
                    shifted += 26;
                }
                c = (char) (shifted + base);
            }
            decrypted.append(c);
        }
        return decrypted.toString();
    }
}
