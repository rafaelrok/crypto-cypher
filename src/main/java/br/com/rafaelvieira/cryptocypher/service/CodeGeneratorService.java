package br.com.rafaelvieira.cryptocypher.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CodeGeneratorService {

    public String generateVerificationCode() {
        Random random = new Random();
        return String.format("%08d", random.nextInt(100000000));
    }
}
