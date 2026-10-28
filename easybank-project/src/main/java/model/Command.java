package model;
public final class Command {
    private final TransactionType type;
    private final String accountNumber;
    private final long amount;
    public Command(TransactionType type, String accountNumber, long amount) {
        this.type = type;
        this.accountNumber = accountNumber;
        this.amount = amount;
    }
    public TransactionType type() { return type; }
    public String accountNumber() { return accountNumber; }
    public long amount() { return amount; }
}