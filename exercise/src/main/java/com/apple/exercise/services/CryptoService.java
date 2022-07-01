package com.apple.exercise.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.bind.DatatypeConverter;

@Service
public class CryptoService {
    //constant value of base64 string used for the symmetric key
    @Value("${base64.symmetric.key}")
    private String base64SymmetricKey;

    //constant base64 string used for the initialization vector for the AES encryption algorithm
    @Value("${base64.symmetric.initialization.vector}")
    private String base64InitializationVector;

    //variables used for AES symmetric encryption cipher
    private static final String AES = "AES";
    private static final String FORMAT = "RAW";
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING"; //using block cipher CBC mode
    private final SecretKey symmetricKey = new SecretKey() {
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
            return DatatypeConverter.parseBase64Binary(base64SymmetricKey);
        }
    };

    //method used to convert a plain text string to a base64 encrypted byte[] which is then returned in string format
    public String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(
                DatatypeConverter.parseBase64Binary(base64InitializationVector));

        cipher.init(Cipher.ENCRYPT_MODE, symmetricKey, ivParameterSpec);

        return DatatypeConverter.printBase64Binary(cipher.doFinal(plainText.getBytes()));
    }

    //method used to decrypt a given base64 cipher text byte[] and return the plain text version in string format
    public String decrypt(byte[] cipherText) throws Exception
    {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(
                DatatypeConverter.parseBase64Binary(base64InitializationVector));

        cipher.init(
                Cipher.DECRYPT_MODE,
                symmetricKey,
                ivParameterSpec);

        return new String(cipher.doFinal(cipherText));
    }
}
