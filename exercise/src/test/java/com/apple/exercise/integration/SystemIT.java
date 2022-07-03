package com.apple.exercise.integration;

import com.apple.exercise.services.CryptoService;
import com.apple.exercise.services.MathService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.bind.DatatypeConverter;
import java.util.Random;

@SpringBootTest
public class SystemIT {
    @Autowired
    MathService mathService;

    @Autowired
    CryptoService cryptoService;

    @Test
    public void avgIT() throws Exception {
        Random random = new Random();
        long size = 10000;
        float sum = 0;
        float avg = 0;
        String plainTextAvg;

        for(int i = 0; i < size; i++) {
            float input = random.nextFloat();
            sum += input;
            avg = mathService.calculateAvg(sum, size);
            plainTextAvg = cryptoService.decrypt(DatatypeConverter.parseBase64Binary(cryptoService.encrypt(Float.toString(avg))));
            Assertions.assertEquals(
                    Float.toString(avg),
                    plainTextAvg);
        }
    }
}
