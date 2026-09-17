import java.util.HashMap;
import java.util.Map;

/**
 * Q6: Login system with exception-based validation.
 * Custom exceptions:
 *   - InvalidUsernameException
 *   - InvalidPasswordException
 *   - AccountLockedException
 * - Fixed number of login attempts (3); account locked after max failures.
 * - try-catch-finally appropriately used.
 * - Different messages for different failures.
 */
public class LoginSystem {

    private static final int MAX_ATTEMPTS = 3;

    // ---- Custom Exceptions ----
    static class InvalidUsernameException extends Exception {
        public InvalidUsernameException(String username) {
            super("Username '" + username + "' does not exist.");
        }
    }

    static class InvalidPasswordException extends Exception {
        private final int attemptsLeft;

        public InvalidPasswordException(int attemptsLeft) {
            super("Incorrect password. Attempts remaining: " + attemptsLeft);
            this.attemptsLeft = attemptsLeft;
        }
        public int getAttemptsLeft() { return attemptsLeft; }
    }

    static class AccountLockedException extends Exception {
        public AccountLockedException(String username) {
            super("Account '" + username + "' is LOCKED due to too many failed attempts.");
        }
    }

    // ---- User Registry ----
    static class UserAccount {
        private final String username;
        private final String password;
        private int failedAttempts = 0;
        private boolean locked     = false;

        public UserAccount(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public boolean isLocked()        { return locked; }
        public String  getUsername()     { return username; }
        public boolean checkPassword(String pw) { return password.equals(pw); }

        public void recordFailure() {
            failedAttempts++;
            if (failedAttempts >= MAX_ATTEMPTS) locked = true;
        }

        public int attemptsLeft() {
            return Math.max(0, MAX_ATTEMPTS - failedAttempts);
        }

        public void resetFailures() { failedAttempts = 0; }
    }

    // ---- LoginSystem ----
    private final Map<String, UserAccount> users = new HashMap<>();

    public void registerUser(String username, String password) {
        users.put(username, new UserAccount(username, password));
    }

    /**
     * Attempts to log in with the given credentials.
     * Propagates InvalidUsernameException, InvalidPasswordException,
     * or AccountLockedException as appropriate.
     */
    public void login(String username, String password)
            throws InvalidUsernameException, InvalidPasswordException, AccountLockedException {

        // Validate username
        UserAccount account = users.get(username);
        if (account == null) {
            throw new InvalidUsernameException(username);
        }

        // Check if locked
        if (account.isLocked()) {
            throw new AccountLockedException(username);
        }

        // Validate password
        if (!account.checkPassword(password)) {
            account.recordFailure();
            if (account.isLocked()) {
                throw new AccountLockedException(username);
            }
            throw new InvalidPasswordException(account.attemptsLeft());
        }

        // Successful login
        account.resetFailures();
        System.out.println("  [LOGIN SUCCESS] Welcome, " + username + "!");
    }

    // ---- Test Helper ----
    private void attemptLogin(String username, String password) {
        System.out.printf("  Attempt -> user='%s' pass='%s': ", username, password);
        try {
            login(username, password);
        } catch (AccountLockedException e) {
            System.out.println("\n  [LOCKED] " + e.getMessage());
        } catch (InvalidUsernameException e) {
            System.out.println("\n  [INVALID USER] " + e.getMessage());
        } catch (InvalidPasswordException e) {
            System.out.println("\n  [WRONG PASSWORD] " + e.getMessage());
        } finally {
            System.out.println("  --- login attempt done ---");
        }
    }

    // ---- Main ----
    public static void main(String[] args) {
        System.out.println("=== Login System with Exception Handling ===\n");
        LoginSystem system = new LoginSystem();
        system.registerUser("alice", "pass123");
        system.registerUser("bob",   "securePass!");

        // Correct login
        System.out.println("Test 1: Valid login");
        system.attemptLogin("alice", "pass123");

        // Non-existent user
        System.out.println("\nTest 2: Non-existent username");
        system.attemptLogin("charlie", "abc");

        // Wrong password — exhaust attempts to lock account
        System.out.println("\nTest 3: Wrong passwords (locking bob's account)");
        system.attemptLogin("bob", "wrong1");
        system.attemptLogin("bob", "wrong2");
        system.attemptLogin("bob", "wrong3");   // triggers lock

        // Attempt on locked account
        System.out.println("\nTest 4: Login on locked account");
        system.attemptLogin("bob", "securePass!");

        System.out.println("\n=== Login system demo complete ===");
    }
}
