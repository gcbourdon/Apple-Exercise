package com.apple.exercise.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.bind.DatatypeConverter;
import java.util.Random;

@SpringBootTest
public class CryptoServiceTest {
    @Autowired
    CryptoService cryptoService;

    //used for testing the encryption and decryption idempotency of the CryptoService
    @Test
    public void encryptDecryptTest() throws Exception {
        int inputSize = 10000;
        Random random = new Random();

        //generating 10,000 random integers to use for the plain text
        //and testing the CryptoService functions
        for(int i = 0; i < inputSize; i++) {
            String plainText = Integer.toString(random.nextInt());

            Assertions.assertEquals(
                    plainText,
                    cryptoService.decrypt(DatatypeConverter.parseBase64Binary(cryptoService.encrypt(plainText))));
        }
    }
}
