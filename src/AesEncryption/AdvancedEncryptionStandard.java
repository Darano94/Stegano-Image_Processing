package AesEncryption;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Random;

public class AdvancedEncryptionStandard {
    private byte[] key;
    private static final String ALGORITHM = "AES";

    //     Encrypts the given plain text
    public byte[] encrypt(String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(plainText.getBytes("UTF-8"));
        return encryptedData;
    }

    //    Decrypts the given byte array
    public String decrypt(byte[] cipherText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        String decryptedData = new String(cipher.doFinal(cipherText));
        return decryptedData;
    }

    //    creates 'random' key for aes encryption
    public static byte[] createRandomKey(int size) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz!$%&/()=?*'#+-:;.,_"; //no § because its 2-bit and then our key would be too long
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < size) { // length of key
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        return salt.toString().getBytes();
    }

    public AdvancedEncryptionStandard(byte[] key) {
        this.key = key;
    }
}