package com.apple.exercise.services;

import com.apple.exercise.CryptoConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@Service
public class CryptoService {
    //variables used for AES symmetric encryption cipher
    private static final String AES = "AES";
    private static final String FORMAT = "RAW";
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING"; //using block cipher CBC mode

    @Value("${symmetric.key}")
    private String symmetricKey;

    //method used to create a secret key
    public SecretKey createAESKey() throws Exception {
        SecureRandom securerandom = new SecureRandom();
        KeyGenerator keygenerator = KeyGenerator.getInstance(AES);
        keygenerator.init(256, securerandom);
        SecretKey key = new SecretKey() {
            @Override
            public String getAlgorithm() {
                return AES;
            }

            @Override
            public String getFormat() {
                return FORMAT;
            }

            @Override
            public byte[] getEncoded() {
                return DatatypeConverter.parseBase64Binary(symmetricKey);
            }
        };
        System.out.println(key.getAlgorithm());
        System.out.println(DatatypeConverter.printBase64Binary(key.getEncoded()));
        System.out.println(key.getFormat());
        return key;
    }

    //method used to convert a plain text string to a base64 encrypted byte[] which is then returned in string format
    public String encrypt(String plainText, SecretKey secretKey, byte[] initializationBuffer) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initializationBuffer);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        return DatatypeConverter.printBase64Binary(cipher.doFinal(plainText.getBytes()));
    }

    //method used to decrypt a given base64 cipher text byte[] and return the plain text version in string format
    public String decrypt(byte[] cipherText, SecretKey secretKey, byte[] initializationVector) throws Exception
    {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initializationVector);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        return new String(cipher.doFinal(cipherText));
    }
}
