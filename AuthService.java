package securelogin;


import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class AuthService {
    private Map<String, User> users = new HashMap<>();
    private static final String USERS_FILE = "users.txt";
    private static final String LOG_FILE = "log.txt";
    private static final int MAX_ATTEMPTS = 3;

    public AuthService() { load(); }

    public String register(String username, String password) {
        if (username.isEmpty() || username.contains(","))
            return "Invalid username.";
        if (users.containsKey(username))
            return "Username already exists.";
        if (!PasswordUtil.isStrong(password))
            return "Weak password: min 8 chars, uppercase, number, symbol.";
        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hash(password, salt);
        users.put(username, new User(username, hash, salt, 0, false));
        save();
        log(username, "REGISTER");
        return "Registered successfully.";
    }

    public String login(String username, String password) {
        User u = users.get(username);
        if (u == null) {
            log(username, "FAIL - unknown user");
            return "Invalid username or password.";
        }
        if (u.isLocked()) {
            log(username, "BLOCKED - account locked");
            return "Account is locked.";
        }
        if (PasswordUtil.hash(password, u.getSalt()).equals(u.getHash())) {
            u.setFailedAttempts(0);
            save();
            log(username, "SUCCESS");
            return "Login successful.";
        }
        u.setFailedAttempts(u.getFailedAttempts() + 1);
        if (u.getFailedAttempts() >= MAX_ATTEMPTS) {
            u.setLocked(true);
            log(username, "LOCKED after 3 failed attempts");
            save();
            return "Account locked.";
        }
        save();
        log(username, "FAIL - wrong password");
        return "Invalid username or password.";
    }

    private void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User u : users.values())
                pw.println(u.getUsername() + "," + u.getHash() + "," + u.getSalt()
                        + "," + u.getFailedAttempts() + "," + u.isLocked());
        } catch (IOException e) {
            System.out.println("Error saving users.");
        }
    }

    private void load() {
        File f = new File(USERS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                users.put(p[0], new User(p[0], p[1], p[2],
                        Integer.parseInt(p[3]), Boolean.parseBoolean(p[4])));
            }
        } catch (IOException e) {
            System.out.println("Error loading users.");
        }
    }

    private void log(String username, String event) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            pw.println(LocalDateTime.now() + " | " + username + " | " + event);
        } catch (IOException e) {
            System.out.println("Error writing log.");
        }
    }
}