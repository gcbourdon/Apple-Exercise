package com.apple.exercise.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AggregateTest {
    @Test
    public void givenAvgCalculationTest() {
        final float avg = (float)5.4;
        float sum = 0;
        int[] input = {4, 7, 6, 9, 1};

        for(int num: input) {
            sum += num;
        }
        Assertions.assertEquals(avg, (sum/input.length));
    }

    @Test
    public void negativeAvgCalculationTest() {
        final float avg = (float) -3.0;
        float sum = 0;
        int[] input = {-1, -2, -3, -4, -5};

        for(int num: input) {
            sum += num;
        }
        Assertions.assertEquals(avg, (sum/input.length));
    }

    @Test
    public void mixAvgCalculationTest() {
        final float avg = (float) 1.2;
        float sum = 0;
        int[] input = {-1, 4, -2, 7, -3, 6, -4, 9, -5, 1};

        for(int num: input) {
            sum += num;
        }
        Assertions.assertEquals(avg, (sum/input.length));
    }

    @Test
    public void givenStdDevCalculationTest() {
        final String format = "%.3f";
        final float stdDev = (float)2.728;
        float sumOfValues = 0;
        float sumOfSquares = 0;
        int[] input = {4, 7, 6, 9, 1};

        for(int num: input) {
            sumOfValues += num;
            sumOfSquares += Math.pow(num, 2);
        }

        float result = Float.parseFloat(
                String.format(
                        format,
                        (float)Math.sqrt((sumOfSquares/input.length) - Math.pow(sumOfValues/input.length, 2))));

        Assertions.assertEquals(stdDev, result);
    }

    @Test
    public void negativeStdDevCalculationTest() {
        final String format = "%.3f";
        final float stdDev = (float)1.414;
        float sumOfValues = 0;
        float sumOfSquares = 0;
        int[] input = {-1, -2, -3, -4, -5};

        for(int num: input) {
            sumOfValues += num;
            sumOfSquares += Math.pow(num, 2);
        }

        float result = Float.parseFloat(
                String.format(
                        format,
                        (float)Math.sqrt((sumOfSquares/input.length) - Math.pow(sumOfValues/input.length, 2))));

        Assertions.assertEquals(stdDev, result);
    }

    @Test
    public void mixStdDevCalculationTest() {
        final String format = "%.3f";
        final float stdDev = (float)4.729;
        float sumOfValues = 0;
        float sumOfSquares = 0;
        int[] input = {-1, 4, -2, 7, -3, 6, -4, 9, -5, 1};

        for(int num: input) {
            sumOfValues += num;
            sumOfSquares += Math.pow(num, 2);
        }

        float result = Float.parseFloat(
                String.format(
                        format,
                        (float)Math.sqrt((sumOfSquares/input.length) - Math.pow(sumOfValues/input.length, 2))));

        Assertions.assertEquals(stdDev, result);
    }
}
