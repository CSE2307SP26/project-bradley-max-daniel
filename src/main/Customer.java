package main;

import java.util.*;

public class Customer {
    private String username;
    private List<BankAccount> bankAccounts;
    private Mortgage mortgage;
    private int creditScore;

    public Customer(String username) {
        this.username = username;
        this.bankAccounts = new ArrayList<>();
        this.mortgage = null;
        this.creditScore = 650; // Default credit score
    }

    public String getUsername() {
        return username;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public void addBankAccount(BankAccount account) {
        if (this.bankAccounts == null) {
            bankAccounts = new ArrayList<>();
        }
        bankAccounts.add(account);
    }

    public boolean removeAccount(int accountNumber) {
        return bankAccounts.removeIf(account -> account.getAccountNumber() == accountNumber);
    }

    public BankAccount getBankAccount(int accountNumber) {
        for (BankAccount account : bankAccounts) {
            if (account.getAccountNumber() == accountNumber) {
                return account;
            }
        }
        return null;
    }

    public double checkAccountBalance(int accountNumber) {
        BankAccount account = this.getBankAccount(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException();
        }
        return account.getBalance();
    }

    public void withdraw(int accountNumber, double amountToWithdraw) {
        BankAccount account = this.getBankAccount(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException();
        }
        account.withdraw(amountToWithdraw);
    }

    public List<Transaction> getTransactionHistory(int accountNumber) {
        BankAccount account = getBankAccount(accountNumber);
        if (account == null) {
            return new ArrayList<>();
        }
        return account.getTransactionHistory();
    }

    public void applyForMortgage(double loanAmount, double annualRate, double termYears) {
        if (hasMortgage()) {
            throw new IllegalStateException("Customer already has a mortgage");
        }

        this.mortgage = new Mortgage(loanAmount, annualRate, termYears);
    }

    public boolean hasMortgage() {
        return mortgage != null;
    }

    public Mortgage getMortgage() {
        return mortgage;
    }

    public void makeMortgagePayment(int accountNumber, double amount) {
        if (!hasMortgage()) {
            throw new IllegalStateException("Customer does not have a mortgage");
        }

        BankAccount account = getBankAccount(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Bank account not found");
        }

        account.withdraw(amount);
        mortgage.makePayment(amount);

        if (mortgage.isPaidOff()) {
            mortgage = null;
        }
    }

    public int getCreditScore() {
        return creditScore;
    }

}
