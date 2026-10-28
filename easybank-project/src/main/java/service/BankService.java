package service;
import exception.*;
import model.*;
import model.annotation.Audit;
import util.*;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
public class BankService {
    private final Repository<Account> repository=new Repository<Account>();
    private final ConcurrentHashMap<String,Account> accounts=new ConcurrentHashMap<String,Account>();
    private final AccountStore store=new AccountStore();
    private final Path stateFile=Paths.get("data","accounts.bin");
    private final Path logFile=Paths.get("data","logs","transactions.log");
    public BankService(){try{for(Account a:StatePersister.load(stateFile)) register(a);}catch(IOException e){System.err.println("Could not load saved accounts: "+e.getMessage());}}
    private void register(Account account){Account.syncCounterFrom(account);repository.save(account);accounts.put(account.getAccountNumber(),account);store.add(account);}
    public synchronized Account openAccount(String owner, String type, long openingBalance) throws BankException {
        if(owner==null||owner.trim().isEmpty()) throw new BankException("Account holder name is required");
        if(openingBalance<0) throw new InvalidAmountException("Opening balance cannot be negative");
        Account account;
        String t = type.toUpperCase();
        if (t.equals("SAVINGS")) account = new SavingsAccount(owner,openingBalance);
        else if (t.equals("CURRENT")) account = new CurrentAccount(owner,openingBalance,10000);
        else if (t.equals("FIXED") || t.equals("FIXED_DEPOSIT")) account = new FixedDepositAccount(owner,openingBalance);
        else throw new BankException("Unknown account type. Use SAVINGS, CURRENT or FIXED");
        register(account); log(account,"OPEN",openingBalance); return account;
    }
    public Account findAccount(String number) throws AccountNotFoundException {Account a=accounts.get(number);if(a==null)throw new AccountNotFoundException("No account found with number "+number);return a;}
    @Audit("deposit") public void deposit(String number,long amount) throws BankException {Account a=findAccount(number);a.deposit(amount);log(a,"DEPOSIT",amount);}
    @Audit("withdraw") public void withdraw(String number,long amount) throws BankException {Account a=findAccount(number);a.withdraw(amount);log(a,"WITHDRAW",amount);}
    @Audit("transfer") public void transfer(String from,String to,long amount) throws BankException {Account source=findAccount(from),destination=findAccount(to);source.transfer(destination,amount);log(source,"TRANSFER_OUT",amount);log(destination,"TRANSFER_IN",amount);}
    public String statement(String number) throws AccountNotFoundException {return StatementFormatter.buildStatement(findAccount(number));}
    public ArrayList<Account> listAccounts(){ArrayList<Account> list=new ArrayList<Account>(accounts.values());list.sort(Comparator.comparing(Account::getAccountNumber));return list;}
    public String report() throws IOException {return ReportGenerator.generate(logFile.getParent(),Paths.get("data","report.txt"));}
    public synchronized void save() throws IOException {Account[] arr = accounts.values().toArray(new Account[0]); StatePersister.save(arr,stateFile);}
    public Repository<Account> getRepository(){return repository;}
    public ConcurrentHashMap<String,Account> getAccounts(){return accounts;}
    public AccountStore getStore(){return store;}
    private void log(Account a,String type,long amount){try{TransactionLog.append(logFile,LocalDateTime.now()+"|"+type+"|"+amount+"|"+a.getAccountNumber()+"|"+a.getBalance());store.addHistory(type+" "+amount+" for "+a.getAccountNumber());}catch(IOException e){System.err.println("Warning: transaction succeeded but log could not be written: "+e.getMessage());}}
}