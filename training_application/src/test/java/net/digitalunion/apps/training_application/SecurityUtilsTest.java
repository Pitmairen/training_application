
package net.digitalunion.apps.training_application;

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
public class SecurityUtilsTest {
    

    /**
     * Test of hashPassword method, of class SecurityUtils.
     */
    @org.junit.Test
    public void testHashPassword() throws Exception {
        
        String plainTextPassword = "test123";
        String result = SecurityUtils.hashPassword(plainTextPassword);
        assertFalse(result.equals(plainTextPassword));
        
    }

    /**
     * Test of checkPassword method, of class SecurityUtils.
     */
    @org.junit.Test
    public void testCheckPasswordOK() throws Exception {
        
        String plainTextPassword = "test123";
        String hash = SecurityUtils.hashPassword(plainTextPassword);
        
        boolean expResult = true;
        boolean result = SecurityUtils.checkPassword(hash, plainTextPassword);
        
        assertEquals(expResult, result);
       
    }
    
    /**
     * Test of checkPassword method, of class SecurityUtils.
     */
    @org.junit.Test
    public void testCheckPasswordFail() throws Exception {
        
        String plainTextPassword = "test123";
        String hash = SecurityUtils.hashPassword(plainTextPassword);
        
        boolean expResult = false;
        boolean result = SecurityUtils.checkPassword(hash, "hallo1234");
        
        assertEquals(expResult, result);
       
    }
    
}
