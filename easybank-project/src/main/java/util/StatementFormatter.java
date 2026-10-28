package util;
import model.Account;
public final class StatementFormatter {
    private StatementFormatter() {}
    public static String buildStatement(Account account) {
        StringBuilder sb=new StringBuilder(); sb.append("========== MINI BANK STATEMENT ==========\n");
        sb.append("Account number : ").append(account.getAccountNumber()).append('\n');
        sb.append("Account type   : ").append(account.getClass().getSimpleName()).append('\n');
        sb.append("Account holder : ").append(account.getOwnerName()).append('\n');
        sb.append("Balance        : ₹").append(account.getBalance()).append('\n');
        sb.append("Interest rate  : ").append(account.interestRate()).append("% per year\n");
        sb.append("=========================================\n"); return sb.toString();
    }
}



