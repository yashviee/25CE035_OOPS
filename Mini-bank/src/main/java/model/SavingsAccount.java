package model;
public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;
    private final long minBalance;
    public SavingsAccount(String ownerName, long openingBalance, long minBalance) { super(ownerName, openingBalance); if(minBalance<0) throw new IllegalArgumentException("Minimum balance cannot be negative"); this.minBalance=minBalance; }
    public SavingsAccount(String ownerName, long openingBalance) { this(ownerName, openingBalance, 500); }
    @Override public double interestRate() { return 4.0; }
    @Override public boolean canWithdraw(long amount) { return getBalance()-amount >= minBalance; }
    public long getMinBalance() { return minBalance; }
}



