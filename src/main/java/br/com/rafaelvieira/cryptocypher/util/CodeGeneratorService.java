package br.com.rafaelvieira.cryptocypher.util;

import java.util.Random;


public class CodeGeneratorService {

    public String generateVerificationCode() {
        Random random = new Random();
        return String.format("%08d", random.nextInt(100000000));
    }
}
