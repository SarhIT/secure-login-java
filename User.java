package securelogin;

public class User {
    private String username, hash, salt;
    private int failedAttempts;
    private boolean locked;

    public User(String username, String hash, String salt, int failedAttempts, boolean locked) {
        this.username = username;
        this.hash = hash;
        this.salt = salt;
        this.failedAttempts = failedAttempts;
        this.locked = locked;
    }
    public String getUsername() { return username; }
    public String getHash() { return hash; }
    public String getSalt() { return salt; }
    public int getFailedAttempts() { return failedAttempts; }
    public void setFailedAttempts(int n) { failedAttempts = n; }
    public boolean isLocked() { return locked; }
    public void setLocked(boolean l) { locked = l; }
}