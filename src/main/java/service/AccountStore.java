package service;
import model.Account;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
public class AccountStore {
    private final Map<String,Account> accounts=new HashMap<>();
    private final ArrayList<String> transactionHistory=new ArrayList<>();
    public synchronized void add(Account account){accounts.put(account.getAccountNumber(),account);}
    public synchronized Account get(String number){return accounts.get(number);}
    public synchronized ArrayList<Account> getAll(){return new ArrayList<>(accounts.values());}
    public synchronized ArrayList<Account> sortedByBalance(){ArrayList<Account> list=getAll();list.sort(Comparator.comparingLong(Account::getBalance));return list;}
    public synchronized void addHistory(String entry){transactionHistory.add(entry);}
    public synchronized ArrayList<String> getTransactionHistory(){return new ArrayList<>(transactionHistory);}
}



