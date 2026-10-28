package exception;
public class InsufficientFundsException extends BankException {
    private static final long serialVersionUID = 1L; private final long shortfall;
    public InsufficientFundsException(String message, long shortfall) { super(message); this.shortfall=Math.max(0,shortfall); }
    public long getShortfall() { return shortfall; }
}



