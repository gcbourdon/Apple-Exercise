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

    public CryptoController(
            @Autowired CryptoService cryptoService,
            @Autowired MathService mathService) throws Exception {
        this.cryptoService = cryptoService;
        this.mathService = mathService;
        this.sumOfSquares = 0;
        this.sumOfValues = 0;
        this.count = 0;
        this.plainTextStatistics = PlainTextStatistics.builder() //creating a new object to store the running avg and stddev throughout duration of the app
                .avg(0)
                .stdDev(0)
                .build();
    }

    /**
     * method used to update the servers running values without needing to store each input in a data structure
     *
     * @param newVal next input number
     */
    private void updateValues(float newVal) {
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
    public ResponseEntity<PlainTextStatistics> pushAndRecalculate(@RequestBody Map<String, Float> body) {
        updateValues(body.get("value")); //updating server values with new input
        return new ResponseEntity<PlainTextStatistics>(plainTextStatistics, HttpStatus.OK); //return the JSON object
    }

    /**
     * API for adding a new value to update the aggregates and encrypting the new aggregates.
     * The encrypted statistics can be used to find the numeric value of the running average and standard deviation
     * using the Decrypt method below.
     *
     * @param body a map which contains the String "value" and the input number
     * @return a JSON object of type EncryptedStatistics which contains the encryptedAvg and encryptedStdDev
     */
    @PostMapping(path = "/PushRecalculateAndEncrypt", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<EncryptedStatistics> pushRecalculateAndEncrypt(@RequestBody Map<String, Float> body) throws Exception {
        updateValues(body.get("value")); //updating server values with the new input

        return new ResponseEntity<EncryptedStatistics>(
                EncryptedStatistics.builder()
                        .encryptedAvg(cryptoService.encrypt(Float.toString(plainTextStatistics.getAvg())))
                        .encryptedStdDev(cryptoService.encrypt(Float.toString(plainTextStatistics.getStdDev())))
                        .build(), HttpStatus.OK);

    }

    /**
     * API for consuming the running numeric aggregate values by decrypting the previously encrypted values.
     *
     * @param body a map which contains the String "cipher" and encrypted(avg) or encrypted(stdDev)
     * @return a response entity with the String of the actual average or actual standard deviation in the body
     */
    @GetMapping(path = "/Decrypt", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> decrypt(@RequestBody Map<String, String> body) throws Exception {
        String response = cryptoService.decrypt(DatatypeConverter.parseBase64Binary(body.get("cipher")));
        if(response == null) {
            return new ResponseEntity<String>(
                    "cipher text cannot be decrypted because of the following:\n " +
                            "- BadPaddingException\n " +
                            "- InvalidKeyException\n " +
                            "- InvalidAlgorithmParameterException\n " +
                            "- IllegalBlockSizeException\n",
                    HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
