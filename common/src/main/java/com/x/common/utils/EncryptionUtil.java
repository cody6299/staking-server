package com.x.common.utils;

import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import java.security.spec.InvalidKeySpecException;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.util.io.pem.PemReader;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.openssl.PEMParser;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import jline.console.ConsoleReader;
import javax.crypto.KeyGenerator;
import lombok.extern.slf4j.Slf4j;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.crypto.Cipher;
import java.security.*;
import lombok.Data;
import java.util.*;
import java.io.*;

@Slf4j
public class EncryptionUtil {

    static { 
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public static String calcPasswordHash(String salt, String password) {
        return DigestUtils.md5Hex(DigestUtils.md5Hex(password) + salt);
    }

    public static String readPassword(String prompt, int maxLength, boolean needRepeat) throws IOException {
        String password;
        String passwordAgain = null;

        ConsoleReader consoleReader = new ConsoleReader();
        consoleReader.setExpandEvents(false);
        do {
            password = consoleReader.readLine(prompt + ": ", '*');

            if (needRepeat) {
                passwordAgain = consoleReader.readLine(prompt + " again: ", '*');

                if (!password.equals(passwordAgain)) {
                    System.out.println("Two passwords are different. Try again.");
                    continue;
                }
            }
            if (password.length() > maxLength) {
                System.out.println("The password length must be less than " + maxLength);
            }
        } while (needRepeat && !password.equals(passwordAgain));

        return password;
    }

    @Data
    public static class PasswordHash implements Serializable {
        private String salt;
        private String hash;

        public PasswordHash(String salt, String hash) {
            this.salt = salt;
            this.hash = hash;
        }
    }

    public static void writePasswordHash(String coin, String password, String file) throws Exception {
        String salt = RandomStringUtils.randomAlphabetic(8);
        String hash = calcPasswordHash(salt, password);

        PasswordHash passwordHash = new PasswordHash(salt, hash);
        Map<String, PasswordHash> hashMap;
        if (Files.exists(Paths.get(file))) {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
            hashMap = (HashMap<String, PasswordHash>) inputStream.readObject();
            inputStream.close();
        } else {
            hashMap = new HashMap<>();
        }
        hashMap.put(coin, passwordHash);
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
        outputStream.writeObject(hashMap);
        outputStream.close();
    }

    private static byte[] pad(byte[] data) {
        int blockSize = 32;
        byte[] padded = new byte[blockSize - data.length % blockSize + data.length];
        System.arraycopy(data, 0, padded, 0, data.length);
        for (int i = data.length; i < padded.length; i++) {
            padded[i] = (byte) 0;
        }

        return padded;
    }

    public static byte[] decryptAES(String cipherText, String password) throws GeneralSecurityException {
        byte[] cipherBytes = Base64.decodeBase64(cipherText);

        byte[] pwdBytes = pad(password.getBytes());
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

        SecretKeySpec sKeySpec = new SecretKeySpec(pwdBytes, "AES");
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec);

        return cipher.doFinal(cipherBytes);
    }

    public static String encryptAES(byte[] plainBytes, String password) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] keyBytes = new byte[32];
        Arrays.fill(keyBytes, (byte) 0);

        System.arraycopy(password.getBytes(), 0, keyBytes, 0, password.length());

        SecretKeySpec sKeySpec = new SecretKeySpec(keyBytes, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec);

        byte[] cipherBytes = cipher.doFinal(plainBytes);
        return Base64.encodeBase64String(cipherBytes);
    }

    private static PrivateKey parsePrivateKey(InputStream pemRepresentation, String passPhrase) throws IOException {

        if ( passPhrase == null ) {
            passPhrase = "";
        }
        try (Reader reader = new InputStreamReader(pemRepresentation);
             PEMParser pemParser = new PEMParser(reader)) {

            final Object object = pemParser.readObject();
            final JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider( "BC" );

            final KeyPair kp;

            if ( object instanceof PEMEncryptedKeyPair)
            {
                // Encrypted key - we will use provided password
                final PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().build( passPhrase.toCharArray() );
                kp = converter.getKeyPair( ( (PEMEncryptedKeyPair) object ).decryptKeyPair( decProv ) );
            }
            else if ( object instanceof PKCS8EncryptedPrivateKeyInfo)
            {
                // Encrypted key - we will use provided password
                try
                {
                    final PKCS8EncryptedPrivateKeyInfo encryptedInfo = (PKCS8EncryptedPrivateKeyInfo) object;
                    final InputDecryptorProvider provider = new JceOpenSSLPKCS8DecryptorProviderBuilder().build( passPhrase.toCharArray() );
                    final PrivateKeyInfo privateKeyInfo = encryptedInfo.decryptPrivateKeyInfo( provider );
                    return converter.getPrivateKey( privateKeyInfo );
                }
                catch ( PKCSException | OperatorCreationException e )
                {
                    throw new IOException( "Unable to decrypt private key.", e );
                }
            }
            else if ( object instanceof PrivateKeyInfo )
            {
                return converter.getPrivateKey( (PrivateKeyInfo) object );
            }
            else
            {
                // Unencrypted key - no password needed
                kp = converter.getKeyPair( (PEMKeyPair) object );
            }
            reader.close();
            pemParser.close();
            return kp.getPrivate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String decryptWithPassphrase(String cipherText, InputStream privateKeyInputStream, String passphrase) throws IOException, GeneralSecurityException {
        PrivateKey privateKey = parsePrivateKey(privateKeyInputStream, passphrase);
        privateKeyInputStream.close();
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decrypted = cipher.doFinal(Base64.decodeBase64(cipherText));
            return new String(decrypted);
        } catch (GeneralSecurityException e) {
            System.out.println("Decrypt message failed" + e);
            return null;
        }
    }

    public static byte[] generateAES() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(256);
        return generator.generateKey().getEncoded();
    }

    public static byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        new Random().nextBytes(bytes);
        return bytes;
    }

    @Data
    public static class AESKey {
        private byte[] keyBytes;
        private byte[] ivBytes;
    }

    public static AESKey generateKey() throws Exception {
        AESKey res = new AESKey();
        res.setKeyBytes(generateAES());
        res.setIvBytes(generateRandomBytes(16));
        return res;
    }

    private static byte[] byteMerger(byte[] first, byte[] second) {
        byte[] bytes = new byte[first.length + second.length];
        System.arraycopy(first, 0, bytes,0, first.length);
        System.arraycopy(second, 0, bytes, first.length, second.length);
        return bytes;
    }

    public static String encryptRSA(InputStream publicKeyInputStream, byte[] bytes, byte[] ivBytes) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        Reader reader = new BufferedReader(new InputStreamReader(publicKeyInputStream));
        PemReader pemReader = new PemReader(reader);

        byte[] pubKey = pemReader.readPemObject().getContent();
        reader.close();

        PublicKey publicKey;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKey);
            publicKey = keyFactory.generatePublic(pubKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }

        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encrypted = cipher.doFinal(byteMerger(bytes, ivBytes));
            return Base64.encodeBase64String(encrypted);
        } catch (GeneralSecurityException e) {
            log.error("Encrypt password failed",e);
            return null;
        }
    }

    public static String encryptAES(byte[] plainBytes, byte[] key, byte[] ivBytes) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        SecretKeySpec sKeySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);

        byte[] cipherBytes = cipher.doFinal(plainBytes);
        return Base64.encodeBase64String(cipherBytes);
    }


    public static String encryptKey(InputStream publicKeyInputStream, AESKey key) throws Exception {
        return encryptRSA(publicKeyInputStream, key.getKeyBytes(), key.getIvBytes());
    }

}
