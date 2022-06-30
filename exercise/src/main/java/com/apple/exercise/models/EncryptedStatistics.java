package com.apple.exercise.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EncryptedStatistics {
    private String encryptedAvg;
    private String encryptedStdDev;
}
