package main;
import java.util.*;


public class Customer {
    private HashMap<Integer, BankAccount> bankAccounts;

    public Customer() {
        this.bankAccounts = new HashMap<>();
    }

    public BankAccount createBankAccount() {
        BankAccount bankAccount = new BankAccount();
        this.bankAccounts.put(bankAccount.getAccountNumber(), bankAccount);
        return bankAccount;
    }

    public boolean closeBankAccount(int accountNumber) {
       return this.bankAccounts.remove(accountNumber) != null;
    }

    public BankAccount getBankAccount(int accountNumber) {
        return this.bankAccounts.get(accountNumber);
    }

    public Collection<BankAccount> getBankAccounts() {
        return this.bankAccounts.values();
    }

    //Added that customers get their account balance
    public double checkAccountBalance(int accountNumber){
        BankAccount account = this.bankAccounts.get(accountNumber);
        if(account == null){
            throw new IllegalArgumentException();
        }
        return account.getBalance();
    }

    // Withdraw Feature

    public void withdraw(int accountNumber, double amountToWithdraw){
        BankAccount account = this.bankAccounts.get(accountNumber);
        if (account == null){
            throw new IllegalArgumentException();
        }
        account.withdraw(amountToWithdraw);
    }
}