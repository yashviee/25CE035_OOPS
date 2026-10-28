package service;

import model.Account;

/** Repeatedly deposits a fixed amount; useful for synchronization practicals. */
public class AccountWorker implements Runnable {
    private final Account account;
    private final int times;
    private final long amount;

    public AccountWorker(Account account, int times, long amount) {
        if (times < 0) throw new IllegalArgumentException("times cannot be negative");
        this.account = account;
        this.times = times;
        this.amount = amount;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " started");
        for (int i = 0; i < times; i++) {
            try {
                account.deposit(amount);
            } catch (exception.InvalidAmountException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }
        System.out.println(Thread.currentThread().getName() + " finished");
    }
}



