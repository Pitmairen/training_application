package no.hials.trainingapp.auth;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Password Hashing functions
 */
public class Security {

    /**
     * Hashes the plaintext password to make is suitable for storing in the DB
     *
     * @param plainText the plain text password
     * @return the password hash
     */
    public static String hashPassword(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    /**
     * Checks that the plain text password matches the hashed password
     *
     * @param plainText the plain text password
     * @param hash the password hash to check against
     * @return true if the plain text password and the hash matches
     */
    public static boolean checkPassword(String plainText, String hash) {
        
        try{
            return BCrypt.checkpw(plainText, hash);
        }catch(IllegalArgumentException e){
            return false;
        }

    }

}
