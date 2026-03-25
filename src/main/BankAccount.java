package main;

import java.util.ArrayList;

public class BankAccount {
    private static int nextAccountNumber = 1;

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
        if (amount > 0) {
            this.balance += amount;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }
    public void transfer(BankAccount targetAccount, double amount) {
        if (amount <= 0 || this.balance < amount) {
            throw new IllegalArgumentException();
        }

        this.balance -= amount;
        targetAccount.balance += amount;

        this.transactionHistory.add(
                new Transaction("Transfer Out", amount, targetAccount.getAccountNumber()));

        targetAccount.transactionHistory.add(
                new Transaction("Transfer In", amount, this.getAccountNumber()));
    }

    public void viewTransactionHistory() {
        for (Transaction t : transactionHistory) {
            System.out.println(t);
        }
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }

    //Added that customers can withdraw from a bank account 
    public void withdraw(double amountToWithdraw){
        double totalBalanceInAccount = this.balance;
        if(amountToWithdraw > totalBalanceInAccount || amountToWithdraw < 0){
            throw new IllegalArgumentException();
        }
        this.balance -= amountToWithdraw;
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