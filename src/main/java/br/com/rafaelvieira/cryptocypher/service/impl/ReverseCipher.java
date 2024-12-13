package br.com.rafaelvieira.cryptocypher.service.impl;

import br.com.rafaelvieira.cryptocypher.service.CryptographyStrategy;

public class ReverseCipher implements CryptographyStrategy {
    @Override
    public String encrypt(String input) {
        return new StringBuilder(input).reverse().toString();
    }

    @Override
    public String decrypt(String input) {
        return encrypt(input); // A operação é a mesma para criptografar e descriptografar
    }
}
