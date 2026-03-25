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

    public BankAccount getBankAccount(int accountNumber) {
        return this.bankAccounts.get(accountNumber);
    }

    public Collection<BankAccount> getBankAccounts() {
        return this.bankAccounts.values();
    }

    public double checkAccountBalance(int accountNumber){
        BankAccount account = this.bankAccounts.get(accountNumber);
        if(account == null){
            throw new IllegalArgumentException();
        }
        return account.getBalance();
    }

    public void withdraw(int accountNumber, double amountToWithdraw){
        BankAccount account = this.bankAccounts.get(accountNumber);
        if (account == null){
            throw new IllegalArgumentException();
        }
        account.withdraw(amountToWithdraw);
    }
}
