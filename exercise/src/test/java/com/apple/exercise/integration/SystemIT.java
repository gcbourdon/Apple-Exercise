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

    //this is a system integration test using both services together
    @Test
    public void avgIT() throws Exception {
        Random random = new Random();
        final long size = 10000;
        long count = 0;
        float sumOfValues = 0;
        float sumOfSquares = 0;
        float avg = 0;
        float stdDev = 0;
        String plainTextAvg;
        String plainTextStdDev;

        //generating 10,000 random numbers for the running avg and stddev
        for(int i = 0; i < size; i++) {
            count++;
            float input = random.nextFloat();
            sumOfValues += input;
            sumOfSquares += (float)Math.pow(input, 2);

            //calculating aggregates using math service
            avg = mathService.calculateAvg(sumOfValues, count);
            stdDev = mathService.calculateStdDev(sumOfSquares, sumOfValues, count);

            //getting plain text values by encrypting the real value then using the decrypt method in crypto service
            plainTextAvg = cryptoService.decrypt(DatatypeConverter.parseBase64Binary(
                    cryptoService.encrypt(Float.toString(avg))));

            plainTextStdDev = cryptoService.decrypt(DatatypeConverter.parseBase64Binary(
                    cryptoService.encrypt(Float.toString(stdDev))));

            Assertions.assertEquals(
                    Float.toString(avg),
                    plainTextAvg);

            Assertions.assertEquals(
                    Float.toString(stdDev),
                    plainTextStdDev
            );

        }
    }
}
