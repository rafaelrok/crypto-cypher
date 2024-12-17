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
            return new VigenereCipher("key");
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

    public String encrypt(String content) throws Exception {
        return createStrategy().encrypt(content);
    }

    public String decrypt(String content) throws Exception {
        return createStrategy().decrypt(content);
    }
}
