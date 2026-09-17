import java.util.HashMap;
import java.util.Map;

/**
 * Q5: Simple Banking Application
 * - Supports deposit and withdrawal
 * - Custom InsufficientBalanceException
 * - Prevents withdrawal when balance is insufficient
 * - Prevents zero or negative deposits/withdrawals
 * - Exception propagation from one method to another
 * - AccountNotFoundException for invalid account numbers
 */
public class BankingApp {

    // ---- Custom Exceptions ----
    static class InsufficientBalanceException extends Exception {
        private final double balance;
        private final double amount;

        public InsufficientBalanceException(double balance, double amount) {
            super(String.format(
                "Insufficient balance. Available: %.2f, Requested: %.2f", balance, amount));
            this.balance = balance;
            this.amount  = amount;
        }
        public double getBalance() { return balance; }
        public double getAmount()  { return amount; }
    }

    static class AccountNotFoundException extends Exception {
        public AccountNotFoundException(String accountId) {
            super("Account not found: " + accountId);
        }
    }

    // ---- BankAccount Model ----
    static class BankAccount {
        private final String accountId;
        private final String owner;
        private double balance;

        public BankAccount(String accountId, String owner, double initialBalance) {
            this.accountId = accountId;
            this.owner     = owner;
            this.balance   = initialBalance;
        }

        public String getAccountId() { return accountId; }
        public String getOwner()     { return owner;     }
        public double getBalance()   { return balance;   }

        /**
         * Deposit amount into the account.
         * Propagates IllegalArgumentException for zero/negative deposits.
         */
        public void deposit(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException(
                    "Deposit amount must be positive. Got: " + amount);
            }
            balance += amount;
            System.out.printf("  [DEPOSIT]  %s | +%.2f | Balance: %.2f%n",
                              accountId, amount, balance);
        }

        /**
         * Withdraw amount from the account.
         * Throws InsufficientBalanceException if balance is too low.
         * Propagates IllegalArgumentException for zero/negative withdrawals.
         */
        public void withdraw(double amount) throws InsufficientBalanceException {
            if (amount <= 0) {
                throw new IllegalArgumentException(
                    "Withdrawal amount must be positive. Got: " + amount);
            }
            if (amount > balance) {
                throw new InsufficientBalanceException(balance, amount);
            }
            balance -= amount;
            System.out.printf("  [WITHDRAW] %s | -%.2f | Balance: %.2f%n",
                              accountId, amount, balance);
        }

        public void printBalance() {
            System.out.printf("  [BALANCE]  %s (%s): %.2f%n", accountId, owner, balance);
        }
    }

    // ---- Bank (account registry) ----
    static class Bank {
        private final Map<String, BankAccount> accounts = new HashMap<>();

        public void addAccount(BankAccount account) {
            accounts.put(account.getAccountId(), account);
        }

        private BankAccount getAccount(String accountId) throws AccountNotFoundException {
            BankAccount acc = accounts.get(accountId);
            if (acc == null) throw new AccountNotFoundException(accountId);
            return acc;
        }

        /** Deposit — propagates IllegalArgumentException and AccountNotFoundException */
        public void deposit(String accountId, double amount) throws AccountNotFoundException {
            BankAccount acc = getAccount(accountId);   // propagates AccountNotFoundException
            acc.deposit(amount);                       // propagates IllegalArgumentException
        }

        /** Withdraw — propagates InsufficientBalanceException */
        public void withdraw(String accountId, double amount)
                throws AccountNotFoundException, InsufficientBalanceException {
            BankAccount acc = getAccount(accountId);
            acc.withdraw(amount);    // exception propagates from BankAccount.withdraw()
        }

        public void printBalance(String accountId) throws AccountNotFoundException {
            getAccount(accountId).printBalance();
        }
    }

    // ---- Main ----
    public static void main(String[] args) {
        System.out.println("=== Simple Banking Application ===\n");
        Bank bank = new Bank();
        bank.addAccount(new BankAccount("ACC001", "Alice", 5000.00));
        bank.addAccount(new BankAccount("ACC002", "Bob",   1200.00));

        // Display initial balances
        System.out.println("Initial Balances:");
        safeBalance(bank, "ACC001");
        safeBalance(bank, "ACC002");

        // Valid transactions
        System.out.println("\nValid Transactions:");
        safeDeposit(bank,   "ACC001", 1500.00);
        safeWithdraw(bank,  "ACC001", 2000.00);
        safeDeposit(bank,   "ACC002",  300.00);
        safeWithdraw(bank,  "ACC002",  500.00);

        // Error scenarios
        System.out.println("\nError Scenarios:");
        safeWithdraw(bank,  "ACC002", 5000.00);   // insufficient balance
        safeDeposit(bank,   "ACC001",  -100.00);   // negative deposit
        safeWithdraw(bank,  "ACC001",   0.00);     // zero withdrawal
        safeDeposit(bank,   "ACC999", 1000.00);    // account not found
        safeWithdraw(bank,  "ACC999", 1000.00);    // account not found

        System.out.println("\nFinal Balances:");
        safeBalance(bank, "ACC001");
        safeBalance(bank, "ACC002");

        System.out.println("\n=== Banking app demo complete ===");
    }

    private static void safeDeposit(Bank bank, String id, double amount) {
        try {
            bank.deposit(id, amount);
        } catch (AccountNotFoundException e) {
            System.out.println("  [ERROR] AccountNotFoundException: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("  [ERROR] IllegalArgumentException: " + e.getMessage());
        }
    }

    private static void safeWithdraw(Bank bank, String id, double amount) {
        try {
            bank.withdraw(id, amount);
        } catch (AccountNotFoundException e) {
            System.out.println("  [ERROR] AccountNotFoundException: " + e.getMessage());
        } catch (InsufficientBalanceException e) {
            System.out.println("  [ERROR] InsufficientBalanceException: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("  [ERROR] IllegalArgumentException: " + e.getMessage());
        }
    }

    private static void safeBalance(Bank bank, String id) {
        try {
            bank.printBalance(id);
        } catch (AccountNotFoundException e) {
            System.out.println("  [ERROR] " + e.getMessage());
        }
    }
}
