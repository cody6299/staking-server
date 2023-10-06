package com.x.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.openssl.EncryptionException;
import org.bouncycastle.pkcs.PKCSException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.*;
import java.io.*;
import lombok.Data;

@Slf4j
public class CryptUtil {

    public static PrivateKey loadRSAKey(InputStream pemRepresentation, String password) throws Exception {
        if ( password == null ) {
            password = "";
        }
        try (Reader reader = new InputStreamReader(pemRepresentation);
             PEMParser pemParser = new PEMParser(reader)) {

            final Object object = pemParser.readObject();
            final JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider( "BC" );

            final KeyPair kp;

            if ( object instanceof PEMEncryptedKeyPair)
            {
                // Encrypted key - we will use provided password
                final PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().build( password.toCharArray() );
                kp = converter.getKeyPair( ( (PEMEncryptedKeyPair) object ).decryptKeyPair( decProv ) );
            }
            else if ( object instanceof PKCS8EncryptedPrivateKeyInfo)
            {
                // Encrypted key - we will use provided password
                try
                {
                    final PKCS8EncryptedPrivateKeyInfo encryptedInfo = (PKCS8EncryptedPrivateKeyInfo) object;
                    final InputDecryptorProvider provider = new JceOpenSSLPKCS8DecryptorProviderBuilder().build( password.toCharArray() );
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
        } catch (EncryptionException e) {
            throw e;
        } catch (Exception e) {
            log.error("load rsa key error", e);
            throw e;
        }
    }

    @Data
    public static class AESKey {
        byte[] iv;
        byte[] key;
    }

    public static byte[] decryptAES(byte[] input, AESKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(key.iv);
        SecretKeySpec sKeySpec = new SecretKeySpec(key.key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);

        return cipher.doFinal(input);
    }

    public static AESKey decryptRSA(PrivateKey privateKey, byte[] cipherBytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(cipherBytes);

        //与signer长度保持一致
        byte[] key = new byte[32];
        System.arraycopy(result, 0, key, 0, 32);
        byte[] iv = new byte[16];
        System.arraycopy(result, 32, iv, 0, 16);

        AESKey aesKey = new AESKey();
        aesKey.key = key;
        aesKey.iv = iv;

        return aesKey;
    }

}
