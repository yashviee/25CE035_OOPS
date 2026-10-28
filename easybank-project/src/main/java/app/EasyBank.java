package app;
import exception.BankException;
import model.*;
import model.annotation.Audit;
import service.BankService;
import service.TransactionProcessor;
import util.AnnotationValidator;
import util.CommandParser;
import util.Validator;
import java.io.IOException;
import java.util.Scanner;
public class EasyBank {
    private static final BankInfo BANK=new BankInfo("SwiftBank", "Main Branch");
    private static final Scanner SCANNER=new Scanner(System.in);
    private static final BankService SERVICE=new BankService();
    public static void main(String[] args) {
        System.out.println("====================================");System.out.println(BANK);System.out.println("Safe, simple console banking");
        boolean running=true;
        while(running){showMenu();int choice=readInt("Choose an option: ");
            try {
                MenuOption option=null;
                if(choice==1) option=MenuOption.OPEN_ACCOUNT;
                else if(choice==2) option=MenuOption.DEPOSIT;
                else if(choice==3) option=MenuOption.WITHDRAW;
                else if(choice==4) option=MenuOption.TRANSFER;
                else if(choice==5) option=MenuOption.LIST_ACCOUNTS;
                else if(choice==6) option=MenuOption.STATEMENT;
                else if(choice==7) option=MenuOption.REPORT;
                else if(choice==8) option=MenuOption.EXIT;
                if(option==null){System.out.println("Please enter a number from 1 to 8.");continue;}
                switch(option){
                    case OPEN_ACCOUNT: openAccount(); break;
                    case DEPOSIT: {String id=readText("Account number: ");long amount=readLong("Deposit amount (₹): ");SERVICE.deposit(id,amount);System.out.println("Deposit successful."); break;}
                    case WITHDRAW: {String id=readText("Account number: ");long amount=readLong("Withdrawal amount (₹): ");SERVICE.withdraw(id,amount);System.out.println("Withdrawal successful."); break;}
                    case TRANSFER: {String from=readText("From account: ");String to=readText("To account: ");long amount=readLong("Amount (₹): ");SERVICE.transfer(from,to,amount);System.out.println("Transfer successful."); break;}
                    case LIST_ACCOUNTS: {if(SERVICE.listAccounts().isEmpty())System.out.println("No accounts yet.");else { for(Account ac : SERVICE.listAccounts()) System.out.println(ac); } break;}
                    case STATEMENT: System.out.println(SERVICE.statement(readText("Account number: "))); break;
                    case REPORT: System.out.println(SERVICE.report()); break;
                    case EXIT: {SERVICE.save();System.out.println("Saved account data. Thank you for banking with EasyBank. Goodbye!");running=false; break;}
                }
            } catch(BankException e){System.out.println("Banking error: "+e.getMessage());if(e instanceof exception.InsufficientFundsException){exception.InsufficientFundsException f=(exception.InsufficientFundsException)e;System.out.println("Shortfall: ₹"+f.getShortfall());}}
              catch(IOException e){System.out.println("File error: "+e.getMessage());}
              catch(IllegalArgumentException e){System.out.println("Input error: "+e.getMessage());}
        }
    }
    private static void showMenu(){System.out.println("\n1. Open account\n2. Deposit\n3. Withdraw\n4. Transfer\n5. List accounts\n6. Account statement\n7. End-of-day report\n8. Exit");}
    private static void openAccount() throws BankException {
        String name=readText("Account holder name: ");String type=readText("Type (SAVINGS/CURRENT/FIXED): ");long balance=readLong("Opening balance (₹): ");
        Account a=SERVICE.openAccount(name,type,balance);System.out.println("Account created: "+a);System.out.println("Keep your account number safe: "+a.getAccountNumber());
    }
    private static String readText(String prompt){System.out.print(prompt);return SCANNER.nextLine().trim();}
    private static int readInt(String prompt){while(true){try{return Integer.parseInt(readText(prompt));}catch(NumberFormatException e){System.out.println("Enter a whole number.");}}}
    private static long readLong(String prompt){while(true){try{return Long.parseLong(readText(prompt));}catch(NumberFormatException e){System.out.println("Enter a whole number amount.");}}}
}