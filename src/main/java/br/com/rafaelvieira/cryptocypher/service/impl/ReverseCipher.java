package br.com.rafaelvieira.cryptocypher.service.impl;

import br.com.rafaelvieira.cryptocypher.service.CryptographyStrategy;

public class ReverseCipher implements CryptographyStrategy {

    @Override
    public String encrypt(String name) {
        StringBuilder encrypted = new StringBuilder(name);
        return encrypted.reverse().toString();
    }

    @Override
    public String decrypt(String encryptedName) {
        StringBuilder decrypted = new StringBuilder(encryptedName);
        return decrypted.reverse().toString();
    }
}
