package main;

public class BankAccount {

    private static int nextAvailableAccountNumber = 1;
    private double balance;
    private int accountNumber;

    public BankAccount() {
        this.accountNumber = nextAvailableAccountNumber++; // increment account number for uniqueness
        this.balance = 0;
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

    //Added that customers can withdraw from a bank account 
    public void withdraw(double amountToWithdraw){
        double totalBalanceInAccount = this.balance;
        if(amountToWithdraw > totalBalanceInAccount || amountToWithdraw < 0){
            throw new IllegalArgumentException();
        }
        this.balance -= amountToWithdraw;
    }

}