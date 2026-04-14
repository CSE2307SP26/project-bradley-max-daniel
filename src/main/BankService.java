package main;

import java.util.List;
import java.util.Scanner;

public class BankService {
    private final Persistence persistence;
    private final Scanner scanner;

    public BankService(Persistence persistence, Scanner scanner) {
        this.persistence = persistence;
        this.scanner = scanner;
    }

    public void applyForMortgage(Customer customer) {
        if (customer.hasMortgage()) {
            System.out.println("You already have a mortgage.");
            return;
        }
        try {
            System.out.print("Enter loan amount: ");
            double loanAmount = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter annual interest rate (example: 0.05 for 5%): ");
            double annualRate = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter term in years: ");
            double termYears = Double.parseDouble(scanner.nextLine());
            customer.applyForMortgage(loanAmount, annualRate, termYears);
            persistence.updateCustomer(customer);
            System.out.println("Mortgage application successful.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter numeric values.");
        } catch (Exception e) {
            System.out.println("Unable to apply for mortgage.");
        }
    }

    public void viewMortgage(Customer customer) {
        if (!customer.hasMortgage()) {
            System.out.println("You do not have a mortgage.");
            return;
        }
        Mortgage mortgage = customer.getMortgage();
        System.out.println("Mortgage Information:");
        System.out.println("Loan Amount: $" + mortgage.getLoanAmount());
        System.out.println("Annual Rate: " + mortgage.getAnnualRate());
        System.out.println("Term (Years): " + mortgage.getTermYears());
        System.out.println("Remaining Balance: $" + mortgage.getRemainingBalance());
    }

    public void makeMortgagePayment(Customer customer) {
        if (!customer.hasMortgage()) {
            System.out.println("You do not have a mortgage.");
            return;
        }
        try {
            System.out.print("Enter bank account number: ");
            int accountNumber = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter payment amount: ");
            double amount = Double.parseDouble(scanner.nextLine());
            customer.makeMortgagePayment(accountNumber, amount);
            persistence.updateCustomer(customer);
            if (customer.hasMortgage()) {
                System.out.println("Mortgage payment successful.");
                System.out.println("Remaining Balance: $" + customer.getMortgage().getRemainingBalance());
            } else {
                System.out.println("Mortgage fully paid off.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Unable to process mortgage payment.");
        }
    }

    public void viewAccounts(Customer customer) {
        List<BankAccount> accounts = customer.getBankAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        for (BankAccount acc : accounts) {
            System.out.println("Account #" + acc.getAccountNumber() + ": Balance $" + acc.getBalance());
        }
    }

    public void createAccount(Customer customer) {
        BankAccount newAccount = new BankAccount();
        customer.addBankAccount(newAccount);
        persistence.updateCustomer(customer);
        System.out.println("New account opened. Account #" + newAccount.getAccountNumber());
    }

    public void deposit(Customer customer) {
        BankAccount account = selectAccount(customer);
        if (account == null) {
            return;
        }
        System.out.print("How much would you like to deposit? ");
        double amount = Double.parseDouble(scanner.nextLine());
        try {
            account.deposit(amount);
            persistence.updateCustomer(customer);
            System.out.println("You have successfully deposited $" + amount);
        } catch (Exception e) {
            System.out.println("ERROR: Unable to deposit. Please try again.");
        }
    }

    public void withdraw(Customer customer) {
        BankAccount account = selectAccount(customer);
        if (account == null) {
            return;
        }
        System.out.print("How much would you like to withdraw? ");
        double amount = Double.parseDouble(scanner.nextLine());
        try {
            account.withdraw(amount);
            persistence.updateCustomer(customer);
            System.out.println("You have successfully withdrawn $" + amount);
        } catch (Exception e) {
            System.out.println("ERROR: Unable to withdraw. Please check your balance and try again.");
        }
    }

    public void transfer(Customer customer) {
        BankAccount fromAccount = selectAccount(customer);
        if (fromAccount == null) {
            return;
        }
        System.out.print("Enter the account number you would like to transfer to: ");
        int targetAccountNumber;
        try {
            targetAccountNumber = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid account number. Please try again.");
            return;
        }
        BankAccount toAccount = null;
        Customer recipient = null;
        toAccount = customer.getBankAccount(targetAccountNumber);
        if (toAccount != null) {
            recipient = customer;
        } else {
            Object[] result = persistence.findBankAccount(targetAccountNumber);
            if (result == null) {
                System.out.println("Invalid account number. Please try again.");
                return;
            }
            recipient = (Customer) result[0];
            toAccount = (BankAccount) result[1];
        }
        System.out.print("How much would you like to transfer: $");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please try again.");
            return;
        }
        try {
            fromAccount.transfer(toAccount, amount);
            persistence.updateCustomer(customer);
            if (recipient != customer) {
                persistence.updateCustomer(recipient);
            }
            System.out.println("Successfully transferred $" + amount + " to account #" + targetAccountNumber);
        } catch (Exception e) {
            System.out.println("Failed to transfer. Please check your balance and try again.");
        }
    }

    public void viewTransactionHistory(Customer customer) {
        BankAccount account = selectAccount(customer);
        if (account == null) {
            return;
        }
        account.viewTransactionHistory();
    }

    public BankAccount selectAccount(Customer customer) {
        List<BankAccount> accounts = customer.getBankAccounts();
        if (accounts.isEmpty()) {
            System.out.println("You don't have any bank accounts. Please create one first.");
            return null;
        }
        while (true) {
            System.out.println("Your bank accounts:\n");
            for (int i = 0; i < accounts.size(); i++) {
                System.out.println((i + 1) + ". Account #" + accounts.get(i).getAccountNumber() + " ($"
                        + accounts.get(i).getBalance() + ")");
            }
            System.out.println((accounts.size() + 1) + ". Cancel");
            System.out.print("\nSelect an account: ");
            String input = scanner.nextLine();
            int selection;
            try {
                selection = Integer.parseInt(input) - 1;
            } catch (Exception e) {
                System.out.println("\nInvalid selection. Please try again.\n");
                continue;
            }
            if (selection == accounts.size()) {
                return null;
            }
            if (selection < 0 || selection >= accounts.size()) {
                System.out.println("\nInvalid selection. Please try again.\n");
                return null;
            }
            return accounts.get(selection);
        }
    }
}
