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
    public void systemTest() throws Exception {
        Random random = new Random();
        final long size = 100;
        long count = 0;
        double sumOfValues = 0;
        double sumOfSquares = 0;
        double avg = 0;
        double stdDev = 0;
        String plainTextAvg;
        String plainTextStdDev;

        //generating 10,000 random numbers for the running avg and stddev
        for(int i = 0; i < size; i++) {
            count++;
            double input = random.nextInt();
            sumOfValues += input;
            sumOfSquares += Math.pow(input, 2);

            //calculating aggregates using math service
            avg = mathService.calculateAvg(sumOfValues, count);
            stdDev = mathService.calculateStdDev(sumOfSquares, sumOfValues, count);

            //getting plain text values by encrypting the real value then using the decrypt method in crypto service
            plainTextAvg = cryptoService.decrypt(DatatypeConverter.parseBase64Binary(
                    cryptoService.encrypt(Double.toString(avg))));

            plainTextStdDev = cryptoService.decrypt(DatatypeConverter.parseBase64Binary(
                    cryptoService.encrypt(Double.toString(stdDev))));

            Assertions.assertEquals(
                    Double.toString(avg),
                    plainTextAvg);

            Assertions.assertEquals(
                    Double.toString(stdDev),
                    plainTextStdDev
            );

        }
    }
}
