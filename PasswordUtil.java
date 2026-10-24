package securelogin;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {
    public static String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hash(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] h = md.digest((salt + password).getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(h);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isStrong(String p) {
        return p.length() >= 8
            && p.matches(".*[A-Z].*")
            && p.matches(".*[0-9].*")
            && p.matches(".*[^A-Za-z0-9].*");
    }
}