package br.com.rafaelvieira.cryptocypher.service;

public interface CryptographyStrategy {
    String encrypt(String content);
    String decrypt(String content);
}
