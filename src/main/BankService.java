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

    public void editAccountNickname(Customer customer) {
        BankAccount account = selectAccount(customer);
        if (account == null) {
            return;
        }

        System.out.print("Enter a nickname for Account #" + account.getAccountNumber() + ": ");
        String nickname = scanner.nextLine();
        account.setNickname(nickname);
        persistence.updateCustomer(customer);
        System.out.println("Successfully updated account #" + account.getAccountNumber() + " to have the nickname "
                + account.getNickname());
    }

    public void applyForMortgage(Customer customer) {
        if (customer.hasMortgage()) {
            System.out.println("You already have a mortgage.");
            return;
        }
        try {
            System.out.print("Enter loan amount: ");
            double loanAmount = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter term in years: ");
            double termYears = Double.parseDouble(scanner.nextLine());
            customer.applyForMortgage(loanAmount, termYears);
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
            String nickname = acc.getNickname();
            String display = "Account #" + acc.getAccountNumber() + ": Balance $" + acc.getBalance();
            if (nickname != null && !nickname.isEmpty()) {
                display += " (" + nickname + ")";
            }
            System.out.println(display);
        }
    }

    public void createAccount(Customer customer) {
        BankAccount newAccount = new BankAccount();
        customer.addBankAccount(newAccount);
        customer.updateCreditScore();
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
            customer.updateCreditScore();
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
            customer.updateCreditScore();
            persistence.updateCustomer(customer);
            System.out.println("You have successfully withdrawn $" + amount);
        } catch (Exception e) {
            System.out.println("ERROR: Unable to withdraw. Please check your balance and try again.");
        }
    }

    public void transfer(Customer customer) {
        BankAccount fromAccount = selectAccount(customer);
        if (fromAccount == null)
            return;

        Integer targetNumber = readTargetAccountNumber();
        if (targetNumber == null)
            return;

        Object[] target = findTransferTarget(customer, targetNumber);
        if (target == null)
            return;

        Double amount = readTransferAmount();
        if (amount == null)
            return;

        completeTransfer(customer, fromAccount, target, amount, targetNumber);
    }

    private Integer readTargetAccountNumber() {
        System.out.print("Enter the account number you would like to transfer to: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid account number. Please try again.");
            return null;
        }
    }

    private Object[] findTransferTarget(Customer customer, int targetNumber) {
        BankAccount toAccount = customer.getBankAccount(targetNumber);

        if (toAccount != null) {
            return new Object[] { customer, toAccount };
        }

        Object[] result = persistence.findBankAccount(targetNumber);

        if (result == null) {
            System.out.println("Invalid account number. Please try again.");
            return null;
        }

        return result;
    }

    private Double readTransferAmount() {
        System.out.print("How much would you like to transfer: $");
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please try again.");
            return null;
        }
    }

    private void completeTransfer(Customer customer, BankAccount fromAccount,
            Object[] target, double amount, int targetNumber) {
        Customer recipient = (Customer) target[0];
        BankAccount toAccount = (BankAccount) target[1];

        try {
            fromAccount.transfer(toAccount, amount);
            customer.updateCreditScore();
            persistence.updateCustomer(customer);

            if (recipient != customer) {
                recipient.updateCreditScore();
                persistence.updateCustomer(recipient);
            }

            System.out.println("Successfully transferred $" + amount +
                    " to account #" + targetNumber);

        } catch (Exception e) {
            System.out.println("Failed to transfer. Please check your balance and try again.");
        }
    }

    public void viewTransactionHistory(Customer customer) {
        BankAccount account = selectAccount(customer);
        if (account == null) {
            return;
        }

        while (true) {
            System.out.println("\n===============================\n");
            System.out.println("Please select an option before viewing your account's transaction history:\n");
            System.out.println("1. View ALL transactions");
            System.out.println("2. View deposits only");
            System.out.println("3. View withdrawals only");
            System.out.println("4. View transfers IN only");
            System.out.println("5. View transfers OUT only");
            System.out.println("6. Cancel");
            System.out.print("\nChoose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    account.viewTransactionHistory(null);
                    return;
                case "2":
                    account.viewTransactionHistory("Deposit");
                    return;
                case "3":
                    account.viewTransactionHistory("Withdrawal");
                    return;
                case "4":
                    account.viewTransactionHistory("Transfer In");
                    return;
                case "5":
                    account.viewTransactionHistory("Transfer Out");
                    return;
                case "6":
                    return;
                default:
                    System.out.println("Invalid selection. Please try again.");
            }
        }
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
