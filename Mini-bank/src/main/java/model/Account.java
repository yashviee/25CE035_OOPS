package model;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
import model.annotation.Positive;
import java.io.Serializable;
import java.util.Objects;
public abstract class Account implements Transactable, InterestBearing, Serializable, Comparable<Account> {
    private static final long serialVersionUID = 1L;
    private static long accountCounter = 0;
    @Id @model.annotation.MaxLength(12) private final String accountNumber;
    private String ownerName;
    @Positive(message="balance must not be negative") private long balance;
    private boolean active = true;
    public static synchronized void syncCounterFrom(Account account) {
        String number = account.getAccountNumber();
        if (number.startsWith("AC")) { try { accountCounter = Math.max(accountCounter, Long.parseLong(number.substring(2))); } catch (NumberFormatException ignored) { } }
    }
    protected Account(String ownerName, long openingBalance) {
        if (openingBalance < 0) throw new IllegalArgumentException("Opening balance cannot be negative");
        this.accountNumber = String.format("AC%04d", ++accountCounter); this.ownerName=Objects.requireNonNull(ownerName); this.balance=openingBalance;
    }
    protected Account(String ownerName) { this(ownerName, 0); }
    public abstract boolean canWithdraw(long amount);
    @Override public abstract double interestRate();
    @Override public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException("Deposit amount must be greater than zero");
        balance = Math.addExact(balance, amount);
    }
    @Override public synchronized boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException("Withdrawal amount must be greater than zero");
        if (!active) throw new InsufficientFundsException("Account is inactive", amount);
        if (!canWithdraw(amount)) throw new InsufficientFundsException("Withdrawal not allowed; check balance or account rules", Math.max(0, amount-balance));
        balance -= amount; return true;
    }
    public void transfer(Account to, long amount) throws exception.BankException {
        if (to == null) throw new exception.AccountNotFoundException("Destination account not found");
        if (to == this) throw new exception.BankException("Cannot transfer to the same account");
        Account first = this.accountNumber.compareTo(to.accountNumber) < 0 ? this : to;
        Account second = first == this ? to : this;
        synchronized (first) { synchronized (second) {
            try { withdraw(amount); to.deposit(amount); }
            catch (exception.BankException e) { throw e; }
            catch (ArithmeticException e) { throw new exception.InvalidAmountException("Amount is too large"); }
        }}
    }
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }
    public synchronized long getBalance() { return balance; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active=active; }
    @Override public int compareTo(Account other) { return accountNumber.compareTo(other.accountNumber); }
    @Override public String toString() { return getClass().getSimpleName()+"[accountNumber="+accountNumber+", ownerName="+ownerName+", balance="+getBalance()+"]"; }
    @Override public boolean equals(Object o) { if (this==o) return true; if (!(o instanceof Account)) return false; Account account = (Account) o; return accountNumber.equals(account.accountNumber); }
    @Override public int hashCode() { return accountNumber.hashCode(); }
}