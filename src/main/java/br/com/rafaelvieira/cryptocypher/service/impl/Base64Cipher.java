package br.com.rafaelvieira.cryptocypher.service.impl;

import br.com.rafaelvieira.cryptocypher.service.CryptographyStrategy;
import java.util.Base64;

public class Base64Cipher implements CryptographyStrategy {

    @Override
    public String encrypt(String input) {
        try {
            String encoded = Base64.getEncoder().encodeToString(input.getBytes());
            encoded = encoded.replaceAll("(.{64})", "$1");
            return encoded;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao codificar em Base64: " + e.getMessage(), e);
        }
    }

    @Override
    public String decrypt(String input) {
        try {
            input = input.replaceAll("\\s+", "");

            if (!input.matches("^[A-Za-z0-9+/]*={0,2}$")) {
                throw new IllegalArgumentException("Input não está em formato Base64 válido");
            }

            byte[] decodedBytes = Base64.getDecoder().decode(input);
            return new String(decodedBytes);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Erro ao decodificar Base64: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao decodificar Base64: " + e.getMessage(), e);
        }
    }
}
