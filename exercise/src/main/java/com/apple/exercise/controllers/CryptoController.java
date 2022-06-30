package com.apple.exercise.controllers;

import com.apple.exercise.models.EncryptedStatistics;
import com.apple.exercise.models.PlainTextStatistics;
import com.apple.exercise.services.CryptoService;
import com.apple.exercise.services.MathService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Map;
import javax.xml.bind.DatatypeConverter;

@Controller
@Slf4j
public class CryptoController {
    private CryptoService cryptoService;
    private MathService mathService;
    private PlainTextStatistics plainTextStatistics;
    private float sumOfSquares;
    private float sumOfValues;
    private long count;
    private final SecretKey key;
    private final byte[] initializationBuffer;

    public CryptoController(
            @Autowired CryptoService cryptoService,
            @Autowired MathService mathService) throws Exception {
        this.cryptoService = cryptoService;
        this.mathService = mathService;
        this.sumOfSquares = 0;
        this.sumOfValues = 0;
        this.count = 0;
        this.key = cryptoService.createAESKey();
        this.initializationBuffer = createInitializationBuffer();
        this.plainTextStatistics = PlainTextStatistics.builder()
                .avg(0)
                .stdDev(0)
                .build();
    }

    /**
     * method used to create an initial buffer which is used in the AES encryption algorithm
     * @return the randomly initialized buffer
     */
    private static byte[] createInitializationBuffer()
    {
        //Used with the encryption algorithm
        byte[] buffer = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(buffer);
        return buffer;
    }

    /**
     * method used to update the servers running values.
     * without needing to store each input number, it takes much less memory to calculate the aggregate functions
     *
     * @param newVal next input number
     */
    private void updateValues(int newVal) {
        sumOfValues += newVal;
        sumOfSquares += Math.pow(newVal, 2);
        count++;
        plainTextStatistics.setAvg(mathService.calculateAvg(sumOfValues, count));
        plainTextStatistics.setStdDev(mathService.calculateStdDev(sumOfSquares, sumOfValues, count));
    }

    /**
     * API for adding a new value to update the aggregates.
     *
     * @param body a map which contains the String "value" and the input number
     * @return a JSON object of type PlainTextStatistics which contains the avg and stdDev
     */
    @PostMapping(path = "/PushAndRecalculate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<PlainTextStatistics> pushAndRecalculate(@RequestBody Map<String, Integer> body) {
        updateValues(body.get("value")); //updating server values
        return new ResponseEntity<PlainTextStatistics>(plainTextStatistics, HttpStatus.OK); //return the JSON object
    }

    /**
     * API for adding a new value to update the aggregates and encrypting the new aggregates.
     * The encrypted statistics can be used to find the numeric value of the running average and standard deviation
     * using the Decrypt endpoint below.
     *
     * @param body a map which contains the String "value" and the input number
     * @return a JSON object of type EncryptedStatistics which contains the encryptedAvg and encryptedStdDev
     */
    @PostMapping(path = "/PushRecalculateAndEncrypt", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<EncryptedStatistics> pushRecalculateAndEncrypt(@RequestBody Map<String, Integer> body) throws Exception {
        updateValues(body.get("value")); //updating server values with the new input

        return new ResponseEntity<EncryptedStatistics>(
                EncryptedStatistics.builder()
                        .encryptedAvg(cryptoService.encrypt(Float.toString(plainTextStatistics.getAvg()), key, initializationBuffer))
                        .encryptedStdDev(cryptoService.encrypt(Float.toString(plainTextStatistics.getStdDev()), key, initializationBuffer))
                        .build(), HttpStatus.OK);

    }

    /**
     * API for getting the running numeric aggregate values.
     *
     * @param body a map which contains the String "cipher" and encrypted(avg) or encrypted(stdDev)
     * @return a String of the actual average or actual standard deviation
     */
    @GetMapping(path = "/Decrypt", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String decrypt(@RequestBody Map<String, String> body) throws Exception {
        return cryptoService.decrypt(DatatypeConverter.parseBase64Binary(body.get("cipher")), key, initializationBuffer);
    }
}
