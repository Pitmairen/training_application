/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.hials.trainingapp.auth;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pitmairen
 */
public class SecurityTest {
    

    /**
     * Test of hashPassword method, of class Security.
     */
    @Test
    public void testHashPassword() {

        String plainText = "12345";
        String hash1 = Security.hashPassword(plainText);
        
        assertFalse(plainText.equals(hash1));
        
        String hash2 = Security.hashPassword(plainText);
        
        assertFalse(hash1.equals(hash2));
    }

    /**
     * Test of checkPassword method, of class Security.
     */
    @Test
    public void testCheckPassword() {
        
        String plainText = "12345";
        String hash = Security.hashPassword(plainText);
        boolean result = Security.checkPassword(plainText, hash);
        assertTrue(result);
        
    }
    
    /**
     * Test of checkPassword method, of class Security.
     */
    @Test
    public void testCheckPassword2() {
        
 
        String hash = "$2a$10$w42h6OtYgtUJHyExOzTtiea3xK1LYd2JLlrDwEgJJ.WffF9GJcSjO";
        
        boolean result = Security.checkPassword("1234", hash);
        assertTrue(result);
        
        result = Security.checkPassword("12345", hash);
        assertFalse(result);
    }
    
    
    /**
     * Test of checkPassword method, of class Security.
     */
    @Test
    public void testCheckPasswordIllegalHash() {
        
 
        String hash = "fewafewafewa";
        
        boolean result = Security.checkPassword("1234", hash);
        assertFalse(result);

    }
}
