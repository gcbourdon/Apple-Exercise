package com.apple.exercise.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;


@SpringBootTest
public class MathServiceTest {
    private String format = "%.3f";
    private Random random = new Random();

    @Autowired
    private MathService mathService;

    //used for testing the calculateAvg function in MathService
    @Test
    public void avgTest() {
        //generating 10,000 random numbers to test the running average
        //by keeping track of the sum of values and number of input values
        long size = 10000;
        double sum = 0;

        for(int i = 0; i < size; i++) {
            double num = random.nextInt();
            System.out.println(num);
            sum += num;
        }

        Assertions.assertEquals(
                Double.parseDouble(String.format(format, (sum/size))),
                mathService.calculateAvg(sum, size));
    }

    //used for testing the calculateStdDev function in MathService
    @Test
    public void stdDevTest() {
        //generating 10,000 random numbers to test the running standard deviation
        //by keeping track of the sum of values, sum of squares, and number of input values
        int size = 10000;
        double sumValues = 0;
        double sumSquares = 0;

        for(int i = 0; i < size; i++) {
            double num = random.nextInt();
            sumValues += num;
            sumSquares += Math.pow(num, 2);
        }

        Assertions.assertEquals(
                Double.parseDouble(String.format(
                        format,
                        Math.sqrt((sumSquares/size) - Math.pow((sumValues/size), 2)))),
                mathService.calculateStdDev(sumSquares, sumValues, size));
    }
}
