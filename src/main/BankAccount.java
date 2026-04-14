package main;

import java.util.ArrayList;

public class BankAccount {
    private static int nextAvailableAccountNumber = 1;
    private double balance;
    private int accountNumber;
    private ArrayList<Transaction> transactionHistory;
    private String nickname;

    public BankAccount() {
        this.accountNumber = nextAvailableAccountNumber++; // increment account number for uniqueness
        this.balance = 0;
        this.transactionHistory = new ArrayList<>();
        this.nickname = null;
    }

    // overloaded for Persistence layer
    public BankAccount(int accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
        this.nickname = null;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    //improvement from iteration: shortened transfer method

    public void transfer(BankAccount target, double amount) {
        if (amount <= 0 || balance < amount)
            throw new IllegalArgumentException();

        balance -= amount;
        target.balance += amount;

        try {
            addTransaction("Transfer Out", amount, target);
            target.addTransaction("Transfer In", amount, this);
        } catch (Exception e) {
            balance += amount;
            target.balance -= amount;
            addTransaction("Failed Transfer Out", amount, target);
            throw e;
        }
    }

    private void addTransaction(String type, double amount, BankAccount other) {
        transactionHistory.add(
                new Transaction(type, amount, other.getAccountNumber()));
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
