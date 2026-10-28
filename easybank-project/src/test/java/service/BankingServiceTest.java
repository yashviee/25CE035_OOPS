package service;
import model.SavingsAccount;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class BankingServiceTest {
    public void depositIncreasesBalance() throws Exception {
        SavingsAccount a=new SavingsAccount("Test",500);
        a.deposit(250);
        if(a.getBalance() != 750) throw new AssertionError();
    }
    public void withdrawalRejectedWhenInsufficient() throws Exception {
        SavingsAccount a=new SavingsAccount("Test",500);
        boolean ok=false;
        try{ a.withdraw(2000); }catch(Exception e){ ok=true; }
        if(!ok) throw new AssertionError();
    }
    public void concurrentDeposits() throws Exception {
        SavingsAccount a=new SavingsAccount("Test",0);
        int threads=10; int perThread=100;
        ExecutorService ex=Executors.newFixedThreadPool(threads);
        CountDownLatch latch=new CountDownLatch(threads);
        for(int i=0;i<threads;i++){ ex.submit(new Runnable(){public void run(){for(int j=0;j<perThread;j++){try{a.deposit(1);}catch(Exception ignored){} }latch.countDown();}});}
        latch.await(); ex.shutdown();
        if(a.getBalance() != threads*perThread) throw new AssertionError();
    }
    public static void main(String[] args) throws Exception {
        BankingServiceTest t = new BankingServiceTest();
        t.depositIncreasesBalance();
        t.withdrawalRejectedWhenInsufficient();
        t.concurrentDeposits();
        System.out.println("All tests passed");
    }
}