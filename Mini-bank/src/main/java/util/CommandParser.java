package util;
import model.Command;
import model.TransactionType;
public final class CommandParser {
    private CommandParser() {}
    public static Command parse(String line) {
        if(line==null) throw new IllegalArgumentException("Command cannot be empty");
        String[] parts=line.trim().split("\\s+");
        if(parts.length!=3) throw new IllegalArgumentException("Expected: TYPE ACCOUNT_NUMBER AMOUNT");
        try { TransactionType type=TransactionType.valueOf(parts[0].toUpperCase()); long amount=Long.parseLong(parts[2]); return new Command(type,parts[1],amount); }
        catch(IllegalArgumentException e){throw new IllegalArgumentException("Invalid command type or amount. Example: DEPOSIT AC0001 500",e);}
    }
}



