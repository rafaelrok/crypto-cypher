package br.com.rafaelvieira.cryptocypher.service.impl;

import br.com.rafaelvieira.cryptocypher.service.CryptographyStrategy;

import java.util.Base64;

public class Base64Cipher implements CryptographyStrategy {
    @Override
    public String encrypt(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    @Override
    public String decrypt(String input) {
        byte[] decodedBytes = Base64.getDecoder().decode(input);
        return new String(decodedBytes);
    }
}
