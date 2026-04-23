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

    public void applyForMortgage(double loanAmount, double termYears) {
        if (hasMortgage()) {
            throw new IllegalStateException("Customer already has a mortgage");
        }

        if (creditScore < 600) {
            throw new IllegalStateException("Credit score too low for mortgage approval");
        }

        double annualRate = determineMortgageInterestRate();
        this.mortgage = new Mortgage(loanAmount, annualRate, termYears);
        updateCreditScore();
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
        updateCreditScore();
    }


    public int getCreditScore() {
        return creditScore;
    }

    // Test-only setter for credit score
    public void setCreditScore(int score) {
        this.creditScore = score;
    }

    public void updateCreditScore() {
        int score = 650;

        score += balanceScoreAdjustment();
        score += mortgageScoreAdjustment();

        this.creditScore = clampScore(score);
    }

    private int balanceScoreAdjustment() {
        double totalBalance = getTotalBalance();

        if (totalBalance > 10000)
            return 50;
        if (totalBalance > 5000)
            return 30;
        if (totalBalance < 1000)
            return -50;
        return 0;
    }

    private int mortgageScoreAdjustment() {
        if (hasMortgage()) {
            return mortgage.getRemainingBalance() > 0 ? -20 : 0;
        }
        return 30;
    }

    private double getTotalBalance() {
        double total = 0;
        for (BankAccount acc : bankAccounts) {
            total += acc.getBalance();
        }
        return total;
    }

    private int clampScore(int score) {
        if (score < 300)
            return 300;
        if (score > 850)
            return 850;
        return score;
    }

    private double determineMortgageInterestRate(){
        
        if (creditScore >= 750){
            return .03;
        }

        if (creditScore >= 700){
            return .05;
        }

        return .1;

    }

}
