/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.digitalunion.apps.training_application;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author pitmairen
 */
public class SecurityUtils {
 
    private final static Random randomGenerator;
    static {
        randomGenerator = new Random();
    }
    
    private final static int ITERATIONS = 100000;
    private final static int KEY_LENGTH = 256;
    private final static int VERSION = 1;
    private final static String ALGORITHM = "PBKDF2WithHmacSHA1";
    
    
    
    
    public static String hashPassword(String plainTextPassword) throws NoSuchAlgorithmException, InvalidKeySpecException{
        
        byte[] salt = new byte[16];
        randomGenerator.nextBytes(salt);
        
        return hashPassword(plainTextPassword, salt);
        
    }
    
    
    public static boolean checkPassword(String passwordHash, String plainTextPassword) throws NoSuchAlgorithmException, InvalidKeySpecException{
        
        String[] parts = passwordHash.split(":");

        if(parts.length != 3){
            return false;
        }
        
        Base64.Decoder dec = Base64.getDecoder();
        byte[] salt = dec.decode(parts[1]);
        String newHash = hashPassword(plainTextPassword, salt);
        
        return passwordHash.equals(newHash);
       
    }
    
    
    private static String hashPassword(String plainTextPassword, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException{
        
        KeySpec spec = new PBEKeySpec(plainTextPassword.toCharArray(),
                                      salt, ITERATIONS, KEY_LENGTH);
        
        SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] hash = f.generateSecret(spec).getEncoded();
        
        Base64.Encoder enc = Base64.getEncoder();
        
        return VERSION + ":" +enc.encodeToString(salt) + ":" + enc.encodeToString(hash);

    }
    
    
}
