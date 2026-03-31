package main;

import java.util.ArrayList;

public class BankAccount {
    private static int nextAvailableAccountNumber = 1;
    private double balance;
    private int accountNumber;
    private ArrayList<Transaction> transactionHistory;

    public BankAccount() {
        this.accountNumber = nextAvailableAccountNumber++; // increment account number for uniqueness
        this.balance = 0;
        this.transactionHistory = new ArrayList<>();
    }
    // overloaded for Persistence layer
    public BankAccount(int accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;

            this.transactionHistory.add(new Transaction("Deposit", amount, this.accountNumber));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void transfer(BankAccount targetAccount, double amount) {
        if (amount <= 0 || this.balance < amount) {
            throw new IllegalArgumentException();
        }

        try {
            this.balance -= amount;
            targetAccount.balance += amount;

            this.transactionHistory.add(
                new Transaction("Transfer Out", amount, targetAccount.getAccountNumber()));

            targetAccount.getTransactionHistory().add(
                new Transaction("Transfer In", amount, this.getAccountNumber()));

        } catch (Exception e) {
            this.balance += amount;
            targetAccount.balance -= amount;
            
            this.transactionHistory.add(
                new Transaction("Failed Transfer Out", amount, targetAccount.getAccountNumber()));
            throw e;
        }
    }

    public void withdraw(double amountToWithdraw) {
        double totalBalanceInAccount = this.balance;
        if (amountToWithdraw > totalBalanceInAccount || amountToWithdraw <= 0) {
            throw new IllegalArgumentException();
        }
        this.balance -= amountToWithdraw;

        this.transactionHistory.add(new Transaction("Withdrawal", amountToWithdraw, this.accountNumber));
    }

    public void viewTransactionHistory() {
        for (Transaction t : transactionHistory) {
            System.out.println(t);
        }
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }

    public double getBalance() {
        return this.balance;
    }
}
