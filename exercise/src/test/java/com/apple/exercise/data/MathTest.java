package com.apple.exercise.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MathTest {
    @Test
    public void powerFuncTest() {
        int num = 4;
        int power = 2;
        int result = 16;
        Assertions.assertEquals(16, Math.pow(num, power));
    }

    @Test
    public void negativePowerFuncTest() {
        int num = -4;
        int power1 = 2;
        int power2 = 3;
        int result1 = 16;
        int result2 = -64;

        Assertions.assertEquals(result1, Math.pow(num, power1));
        Assertions.assertEquals(result2, Math.pow(num, power2));
    }
}
