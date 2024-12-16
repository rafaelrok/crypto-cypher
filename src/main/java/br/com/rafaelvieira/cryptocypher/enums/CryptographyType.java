package br.com.rafaelvieira.cryptocypher.enums;

import br.com.rafaelvieira.cryptocypher.service.*;
import br.com.rafaelvieira.cryptocypher.service.impl.*;

public enum CryptographyType {
    AES {
        @Override
        public CryptographyStrategy createStrategy() {
            return new AESCipher();
        }
    },
    DES {
        @Override
        public CryptographyStrategy createStrategy() {
            return new DESCipher();
        }
    },
    RSA {
        @Override
        public CryptographyStrategy createStrategy() {
            return new RSACipher();
        }
    },
    VIGENERE {
        @Override
        public CryptographyStrategy createStrategy() {
            return new VigenereCipher("CHAVE"); // Defina uma chave padrão ou receba como parâmetro
        }
    },
    CAESAR {
        @Override
        public CryptographyStrategy createStrategy() {
            return new CaesarCipher();
        }
    },
    REVERSE {
        @Override
        public CryptographyStrategy createStrategy() {
            return new ReverseCipher();
        }
    },
    BASE64 {
        @Override
        public CryptographyStrategy createStrategy() {
            return new Base64Cipher();
        }
    };

    public abstract CryptographyStrategy createStrategy();

    public String encrypt(String content) {
        return createStrategy().encrypt(content);
    }

    public String decrypt(String content) {
        return createStrategy().decrypt(content);
    }
}

//public enum CryptographyType {
//
//    CAESAR(new CaesarCipher()),
//    REVERSE(new ReverseCipher()),
//    BASE64(new Base64Cipher()),
//    VIGENERE(new VigenereCipher("DEFAULT_KEY")),
//    AES(new AESCipher()),
//    DES(new DESCipher()),
//    RSA(new RSACipher());
//
//    private final CryptographyStrategy strategy;
//
//    CryptographyType(CryptographyStrategy strategy) {
//        this.strategy = strategy;
//    }
//
//    public String encrypt(String input) {
//        return strategy.encrypt(input);
//    }
//
//    public String decrypt(String input) {
//        return strategy.decrypt(input);
//    }
//}
