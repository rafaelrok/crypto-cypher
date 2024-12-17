package br.com.rafaelvieira.cryptocypher.service;

public interface CryptographyStrategy {
    String encrypt(String content) throws Exception;
    String decrypt(String content) throws Exception;
}
