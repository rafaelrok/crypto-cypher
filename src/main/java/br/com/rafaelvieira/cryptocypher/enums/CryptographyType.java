package br.com.rafaelvieira.cryptocypher.enums;

import br.com.rafaelvieira.cryptocypher.service.*;
import br.com.rafaelvieira.cryptocypher.service.impl.*;

public enum CryptographyType {

    CAESAR(new CaesarCipher()),
    REVERSE(new ReverseCipher()),
    BASE64(new Base64Cipher()),
    VIGENERE(new VigenereCipher("DEFAULT_KEY")),
    AES(new AESCipher()),
    DES(new DESCipher()),
    RSA(new RSACipher());

    private final CryptographyStrategy strategy;

    CryptographyType(CryptographyStrategy strategy) {
        this.strategy = strategy;
    }

    public String encrypt(String input) {
        return strategy.encrypt(input);
    }

    public String decrypt(String input) {
        return strategy.decrypt(input);
    }
}
