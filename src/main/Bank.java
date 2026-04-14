package main;

public class Bank {

    public BankAccount openAccount(Customer customer) {
        BankAccount newAccount = new BankAccount();
        customer.addBankAccount(newAccount);
        Persistance.updateCustomer(customer);
        return newAccount;
    }

    public void deposit(Customer customer, BankAccount account, double amount) {
        account.deposit(amount);
        Persistance.updateCustomer(customer);
    }

    public void withdraw(Customer customer, BankAccount account, double amount) {
        account.withdraw(amount);
        Persistance.updateCustomer(customer);
    }

    public void applyForMortgage(Customer customer, double loanAmount, double annualRate, double termYears) {
        customer.applyForMortgage(loanAmount, annualRate, termYears);
        Persistance.updateCustomer(customer);
    }

    public void makeMortgagePayment(Customer customer, int accountNumber, double amount) {
        customer.makeMortgagePayment(accountNumber, amount);
        Persistance.updateCustomer(customer);
    }

    public void transfer(Customer customer, BankAccount fromAccount, int targetAccountNumber, double amount) {
        BankAccount toAccount = null;
        Customer recipient = null;

        toAccount = customer.getBankAccount(targetAccountNumber);
        if (toAccount != null) {
            recipient = customer;
        } else {
            Object[] result = Persistance.findBankAccount(targetAccountNumber);

            if (result == null) {
                throw new IllegalArgumentException("Invalid account number. Please try again.");
            }

            recipient = (Customer) result[0];
            toAccount = (BankAccount) result[1];
        }

        fromAccount.transfer(toAccount, amount);

        Persistance.updateCustomer(customer);

        if (recipient != customer) {
            Persistance.updateCustomer(recipient);
        }
    }
}