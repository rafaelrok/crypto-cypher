package br.com.rafaelvieira.cryptocypher.config;

import br.com.rafaelvieira.cryptocypher.enums.CryptographyType;

import java.util.HashMap;
import java.util.Map;

public class CryptographyConfig {
    private static final Map<CryptographyType, Object> keyStore = new HashMap<>();

    public static void storeKey(CryptographyType type, String key) {
        keyStore.put(type, key);
    }

    public static String getKey(CryptographyType type) {
        return (String) keyStore.get(type);
    }

    public static void clearKeys() {
        keyStore.clear();
    }
}
