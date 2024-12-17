package br.com.rafaelvieira.cryptocypher.service.impl;

import br.com.rafaelvieira.cryptocypher.service.CryptographyStrategy;

public class CaesarCipher implements CryptographyStrategy {
    private static final int SHIFT = 3;

    @Override
    public String encrypt(String input) {
        try {
            StringBuilder encrypted = new StringBuilder();
            for (char c : input.toCharArray()) {
                if (Character.isLetter(c)) {
                    char base = Character.isLowerCase(c) ? 'a' : 'A';
                    c = (char) ((c - base + SHIFT) % 26 + base);
                }
                encrypted.append(c);
            }
            return encrypted.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao codificar Cifra de César: " + e.getMessage(), e);
        }
    }

    @Override
    public String decrypt(String input) {
        try{
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
        } catch (Exception e) {
            throw new RuntimeException("Erro ao decodificar Cifra de César: " + e.getMessage(), e);
        }
    }
}
