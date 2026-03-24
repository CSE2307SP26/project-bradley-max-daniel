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

    public void deposit(double amount) {
        if(amount > 0) {
            this.balance += amount;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public double getBalance() {
        return this.balance;
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }

    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void transfer(BankAccount targetAccount, double amount) {
        if (amount <= 0 || this.balance < amount) {
            throw new IllegalArgumentException();
        }
        
        this.balance -= amount;
        targetAccount.balance += amount;

        this.transactionHistory.add(
            new Transaction("Transfer Out", amount, targetAccount.getAccountNumber())
        );

        targetAccount.transactionHistory.add(
            new Transaction("Transfer In", amount, this.getAccountNumber())
        );
    }

}