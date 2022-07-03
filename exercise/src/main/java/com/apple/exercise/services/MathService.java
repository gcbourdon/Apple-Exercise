package com.apple.exercise.services;

import org.springframework.stereotype.Service;

@Service
public class MathService {
    //used to format each decimal to 3 places
    private final String format = "%.3f";

    /** method used to calculate the running average of the input numbers
     *  by keeping track of the sum of values (SV) and the count of input numbers (C)
     *  this can be used to calculate the average (AVG) = SV/C
     */
    public float calculateAvg(float sumOfValues, long count) {
        float result = (sumOfValues/count);
        return Float.parseFloat(String.format(format, result));
    }

    /** method used to calculate the running standard deviation of the input numbers
     *  keeping track of the sum of squares (SQ), sum of values (SV), and the count of input numbers (C)
     *  the variance (V) = (SQ/C) - (SV/C)^2
     *  this can then be used to calculate the standard deviation (SD) = sqrt(V)
     */
    public float calculateStdDev(float sumOfSquares, float sumOfValues, long count) {
        //dividing by zero is undefined so there must be a check to return 0
        if(count == 0) {
            return 0;
        } else {
            float result = (float)Math.sqrt((sumOfSquares / count) - Math.pow((sumOfValues / count), 2));
            return Float.parseFloat(String.format(format, result));
        }
    }
}
