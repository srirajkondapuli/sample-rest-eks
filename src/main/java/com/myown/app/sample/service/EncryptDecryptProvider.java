package com.myown.app.sample.service;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import com.myown.app.sample.util.CryptoUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * AES-GCM inputs - 12 bytes IV, need the same IV and secret keys for encryption
 * and decryption.
 * <p>
 * The output consist of iv, encrypted content, and auth tag in the following
 * format:
 * output = byte[] {i i i c c c c c c ...}
 * <p>
 * i = IV bytes
 * c = content bytes (encrypted content, auth tag)
 */
@Slf4j
public class EncryptDecryptProvider {

    private static final String OUTPUT_FORMAT = "%-30s: %s";
    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;
    private static final int AES_KEY_BIT = 256;
    private static SecretKey secretKey;
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    // public static Logger logger =
    // LoggerFactory.getLogger(EncryptDecryptProvider.class);
    public EncryptDecryptProvider() {

        try {
            secretKey = CryptoUtil.getAESKey(AES_KEY_BIT);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // AES-GCM needs GCMParameterSpec
    public static byte[] encrypt(byte[] pText, SecretKey secret, byte[] iv) throws Exception {

        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        byte[] encryptedText = cipher.doFinal(pText);
        return encryptedText;

    }

    // prefix IV length + IV bytes to cipher text
    public static byte[] encryptWithPrefixIV(byte[] pText, SecretKey secret, byte[] iv) throws Exception {

        byte[] cipherText = encrypt(pText, secret, iv);

        byte[] cipherTextWithIv = ByteBuffer.allocate(iv.length + cipherText.length)
                .put(iv)
                .put(cipherText)
                .array();
        return cipherTextWithIv;

    }

    public static String decrypt(byte[] cText, SecretKey secret, byte[] iv) throws Exception {

        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        cipher.init(Cipher.DECRYPT_MODE, secret, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        byte[] plainText = cipher.doFinal(cText);
        return new String(plainText, UTF_8);

    }

    public static String decryptWithPrefixIV(byte[] cText, SecretKey secret) throws Exception {

        ByteBuffer bb = ByteBuffer.wrap(cText);

        byte[] iv = new byte[IV_LENGTH_BYTE];
        bb.get(iv);
        // bb.get(iv, 0, iv.length);

        byte[] cipherText = new byte[bb.remaining()];
        bb.get(cipherText);

        String plainText = decrypt(cipherText, secret, iv);
        return plainText;

    }

    public static void maina(String[] args) throws Exception {

        String pText = "Hello World AES-GCM, Welcome to Cryptography!";

        // encrypt and decrypt need the same key.
        // get AES 256 bits (32 bytes) key
        SecretKey secretKey = CryptoUtil.getAESKey(AES_KEY_BIT);

        // encrypt and decrypt need the same IV.
        // AES-GCM needs IV 96-bit (12 bytes)
        byte[] iv = CryptoUtil.getRandomNonce(IV_LENGTH_BYTE);

        EncryptDecryptProvider provider = new EncryptDecryptProvider();
        // byte[] encryptedText =
        // EncryptDecryptProvider.encryptWithPrefixIV(pText.getBytes(UTF_8), secretKey,
        // iv);

        // System.out.println("\n------ AES GCM Encryption ------");
        // System.out.println(String.format(OUTPUT_FORMAT, "Input (plain text)",
        // pText));

        // String encryptedString = Base64.getEncoder().encodeToString(encryptedText);
        // System.out.println(String.format(OUTPUT_FORMAT, "Encrypted (Text) (block =
        // 16)", encryptedString,16));

        // System.out.println("\n------ AES GCM Decryption ------");

        // String decryptedText =
        // EncryptDecryptProvider.decryptWithPrefixIV(Base64.getDecoder().decode(encryptedString),
        // secretKey);

        // System.out.println(String.format(OUTPUT_FORMAT, "Decrypted (plain text)",
        // decryptedText));

        String encString = provider.encrypt(pText);
        String decString = provider.decrypt(encString);

    }

    public String encrypt(String toBeEncrypted) throws Exception {

        log.info("Inside method: encrypt");
        // encrypt and decrypt need the same key.
        // get AES 256 bits (32 bytes) key
        SecretKey secretKey = CryptoUtil.getAESKey(AES_KEY_BIT);

        // encrypt and decrypt need the same IV.
        // AES-GCM needs IV 96-bit (12 bytes)
        byte[] iv = CryptoUtil.getRandomNonce(IV_LENGTH_BYTE);

        byte[] encryptedText = EncryptDecryptProvider.encryptWithPrefixIV(toBeEncrypted.getBytes(UTF_8), this.secretKey,
                iv);

        log.info("\n------ AES GCM Encryption ------");
        log.info(String.format(OUTPUT_FORMAT, "Input (plain text)", toBeEncrypted));

        String encryptedString = Base64.getEncoder().encodeToString(encryptedText);
        log.info(String.format(OUTPUT_FORMAT, "Encrypted (Text) (block = 16)", encryptedString, 16));

        log.info("Returning encrypted credential : {}", encryptedString);
        return encryptedString;
    }

    public String decrypt(String toBeDeCrypted) {
        String decryptedText = "";
        try {
            decryptedText = EncryptDecryptProvider.decryptWithPrefixIV(Base64.getDecoder().decode(toBeDeCrypted),
                    this.secretKey);
            // System.out.println(String.format(OUTPUT_FORMAT, "Decrypted (plain text)",
            // decryptedText));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(String.format(OUTPUT_FORMAT, "Decrypted (plain text)", decryptedText));
        return decryptedText;
    }

}
