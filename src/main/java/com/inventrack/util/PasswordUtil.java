package com.inventrack.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Password hashing helpers using BCrypt.
 *
 * <p>This project stores BCrypt hashes in the {@code users.password} column and verifies login attempts
 * by comparing the provided password against the stored hash.</p>
 */
public class PasswordUtil {
    
    /**
     * Hashes a plain-text password using BCrypt with a cost factor of 12.
     *
     * @param plainTextPassword user-provided password
     * @return BCrypt hash suitable for storage
     */
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
    }
    
    /**
     * Verifies a plain-text password against a stored BCrypt hash.
     *
     * @param plainTextPassword user-provided password
     * @param hashedPassword stored BCrypt hash (e.g. {@code $2a$...})
     * @return true if the password matches; false otherwise
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        // Step 1: Quick rejection if the stored value doesn't look like a BCrypt hash.
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            return false;
        }
        try {
            // Step 2: Delegate verification to BCrypt.
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
