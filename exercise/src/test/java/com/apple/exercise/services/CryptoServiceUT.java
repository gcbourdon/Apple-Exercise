package com.apple.exercise.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class CryptoServiceUT {
    @Value("${base64.secret.key}")
    private String base64SecretKey;

    private final String IVstr = "KNdGxCZ3YrSFmpjJcLrf9Q==";

    @Test
    public void AESSecretKeyTest() {

    }

    @Test
    public void encryptDecryptTest() throws Exception {
        String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING"; //using AES which has 16 byte block size
        IvParameterSpec ivParameterSpec = new IvParameterSpec(DatatypeConverter.parseBase64Binary(IVstr));
        SecretKey secretKey = new SecretKey() {
            @Override
            public String getAlgorithm() {
                return "AES";
            }

            @Override
            public String getFormat() {
                return "RAW";
            }

            @Override
            public byte[] getEncoded() {
                return DatatypeConverter.parseBase64Binary("ozg5AMu6fMoMcNAAWcUBHM0nekNxcGqF3ThJaWAivAg=");
            }
        };

        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        cipher.init(
                Cipher.ENCRYPT_MODE,
                secretKey,
                ivParameterSpec);

        List<String> plainTextInputArray = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            plainTextInputArray.add(Integer.toString(i));
            System.out.println(plainTextInputArray.get(i));
        }

        for(String plainText: plainTextInputArray) {
            byte[] base64encrypted = cipher.doFinal(plainText.getBytes());

            //switch to decrypt mode to verify the decryption using the same secret key and IV works
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            String base64decrypted = new String(cipher.doFinal(base64encrypted));
            Assertions.assertEquals(base64decrypted, plainText);
        }
    }

    @Test
    public void decryptionTest() {

    }
}
