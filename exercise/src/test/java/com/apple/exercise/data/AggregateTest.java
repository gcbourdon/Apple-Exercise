package com.apple.exercise.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AggregateTest {
    @Test
    public void givenAvgCalculationTest() {
        final double avg = 5.4;
        double sum = 0;
        int[] input = {4, 7, 6, 9, 1};

        for(int num: input) {
            sum += num;
        }
        Assertions.assertEquals(avg, (sum/input.length));
    }

    @Test
    public void negativeAvgCalculationTest() {
        final double avg = -3.0;
        double sum = 0;
        int[] input = {-1, -2, -3, -4, -5};

        for(int num: input) {
            sum += num;
        }
        Assertions.assertEquals(avg, (sum/input.length));
    }

    @Test
    public void mixAvgCalculationTest() {
        final double avg = 1.2;
        double sum = 0;
        int[] input = {-1, 4, -2, 7, -3, 6, -4, 9, -5, 1};

        for(int num: input) {
            sum += num;
        }
        Assertions.assertEquals(avg, (sum/input.length));
    }

    @Test
    public void givenStdDevCalculationTest() {
        final String format = "%.3f";
        final double stdDev = 2.728;
        double sumOfValues = 0;
        double sumOfSquares = 0;
        int[] input = {4, 7, 6, 9, 1};

        for(int num: input) {
            sumOfValues += num;
            sumOfSquares += Math.pow(num, 2);
        }

        double result = Double.parseDouble(
                String.format(
                        format,
                        Math.sqrt((sumOfSquares/input.length) - Math.pow(sumOfValues/input.length, 2))));

        Assertions.assertEquals(stdDev, result);
    }

    @Test
    public void negativeStdDevCalculationTest() {
        final String format = "%.3f";
        final double stdDev = 1.414;
        double sumOfValues = 0;
        double sumOfSquares = 0;
        int[] input = {-1, -2, -3, -4, -5};

        for(int num: input) {
            sumOfValues += num;
            sumOfSquares += Math.pow(num, 2);
        }

        double result = Double.parseDouble(
                String.format(
                        format,
                        Math.sqrt((sumOfSquares/input.length) - Math.pow(sumOfValues/input.length, 2))));

        Assertions.assertEquals(stdDev, result);
    }

    @Test
    public void mixStdDevCalculationTest() {
        final String format = "%.3f";
        final double stdDev = 4.729;
        double sumOfValues = 0;
        double sumOfSquares = 0;
        int[] input = {-1, 4, -2, 7, -3, 6, -4, 9, -5, 1};

        for(int num: input) {
            sumOfValues += num;
            sumOfSquares += Math.pow(num, 2);
        }

        double result = Double.parseDouble(
                String.format(
                        format,
                        Math.sqrt((sumOfSquares/input.length) - Math.pow(sumOfValues/input.length, 2))));

        Assertions.assertEquals(stdDev, result);
    }
}
