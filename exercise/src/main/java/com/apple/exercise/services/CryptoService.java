package com.apple.exercise.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class CryptoService {
    // constant value of base64 string used for the symmetric key
    @Value("${base64.secret.key}")
    private String base64SecretKey;

    // variables used for AES symmetric encryption cipher */
    private static final String AES = "AES";
    private static final String FORMAT = "RAW";
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING"; //using AES which has 16 byte block size
    private final byte[] IV;
    private final Cipher cipher;

    // implementing the SecretKey interface using the constant base64 symmetric key defined in properties
    private final SecretKey secretKey = new SecretKey() {
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
            return DatatypeConverter.parseBase64Binary(base64SecretKey);
        }
    };

    public CryptoService() throws NoSuchPaddingException, NoSuchAlgorithmException {
        cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        IV = createInitializationVector();
    }

    /** method used to generate a new IV which is to ensure a different encrypted value for the same input
     *  across multiple program executions. A consequence of this is the encryption/decryption will NOT be idempotent for each
     *  time the program is restarted.
     */
    private byte[] createInitializationVector() {
        //Used with the encryption algorithm to
        byte[] IV = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(IV);
        return IV;
    }

    /** method used to convert a plain text string to a base64 encrypted byte[] which is then returned in string format
     *
     * @param plainText the plain text string of the input number to encrypt
     * @return the cipher text string encoded in base64
     */
    public String encrypt(String plainText) throws Exception {

        //defining the initialization vector parameter spec to encrypt the plaintext differently for each execution while using the same key
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);

        cipher.init(
                Cipher.ENCRYPT_MODE,
                secretKey,
                ivParameterSpec);

        return DatatypeConverter.printBase64Binary(cipher.doFinal(plainText.getBytes()));
    }

    /** method used to decrypt a given base64 cipher text byte[] and return the plain text version in string format
     *
     * @param cipherText the cipher text byte array in base64 encoding scheme
     * @return the decrypted plain text which is the actual numeric value of either total average or total standard deviation
     */
    public String decrypt(byte[] cipherText)  {

        //defining the initialization vector parameter spec using the same IV that was used to encrypt the plain text
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);

        //initializing the cipher to decrypt
        try {
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    secretKey,
                    ivParameterSpec);
        } catch(InvalidKeyException | InvalidAlgorithmParameterException e) {
            return null;
        }

        //returning the plain text
        try {
            return new String(cipher.doFinal(cipherText));
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            return null;
        }
    }
}
