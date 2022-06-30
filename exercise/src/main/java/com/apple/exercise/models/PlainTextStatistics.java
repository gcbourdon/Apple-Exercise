package com.apple.exercise.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlainTextStatistics {
    private float avg;
    private float stdDev;
}
