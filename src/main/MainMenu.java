package main;

import java.util.List;
import java.util.Scanner;

public class MainMenu {
    private static Scanner scanner = new Scanner(System.in);
    private static AuthManager auth = new AuthManager();
    private static final String CUSTOMERS_FILE = "data/customers.json";
    private static Persistence persistence = new Persistence(CUSTOMERS_FILE);

    public static void main(String[] args) {
        final String LOGIN = "1";
        final String REGISTER = "2";
        final String EXIT = "3";

        System.out.println("Welcome to the 237 Bank App!");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            persistence.save();
        }));

        while (true) {
            System.out.println("\n===============================\n");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("\nChoose an option: ");

            String selection = scanner.nextLine();
            System.out.println();

            switch (selection) {
                case LOGIN:
                    login();
                    break;
                case REGISTER:
                    register();
                    break;
                case EXIT:
                    persistence.save();
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void login() {
        System.out.println("Thanks for coming back! Please enter your account details to sign in.\n");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.println();

        try {
            if (!auth.authenticate(username, password)) {
                System.out.println("Invalid username or password.\n");
                return;
            }

            Customer customer = persistence.getCustomer(username);
            if (customer == null) {
                customer = new Customer(username);
                persistence.updateCustomer(customer);
            }

            customerMenu(customer);
        } catch (Exception e) {
            System.out.println("ERROR: Unable to log in. Please try again later.");
        }
    }

    private static void register() {
        System.out.println("Welcome! Please choose a username and password to create an account.\n");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.println();

        try {
            if (!auth.createUser(username, password)) {
                System.out.println("This username is already taken.\n");
                return;
            }

            Customer customer = new Customer(username);
            persistence.updateCustomer(customer);
            System.out.println("Registration successful!\n");

            customerMenu(customer);
        } catch (Exception e) {
            System.out.println("ERROR: Unable to register. Please try again later.");
        }
    }

    private static void customerMenu(Customer customer) {
        final String VIEW_ACCOUNTS = "1";
        final String CREATE_ACCOUNT = "2";
        final String DEPOSIT = "3";
        final String WITHDRAW = "4";
        final String TRANSFER = "5";
        final String VIEW_HISTORY = "6";
        final String APPLY_MORTGAGE = "7";
        final String VIEW_MORTGAGE = "8";
        final String PAY_MORTGAGE = "9";
        final String SIGN_OUT = "10";

        while (true) {
            System.out.println("\n===============================\n");
            System.out.println("\nWelcome, " + customer.getUsername() + "!\n");
            System.out.println("1. View Accounts");
            System.out.println("2. Open New Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. View Transaction History");
            System.out.println("7. Apply for Mortgage");
            System.out.println("8. View Mortgage");
            System.out.println("9. Make Mortgage Payment");
            System.out.println("10. Logout");

            System.out.print("\nChoose an option: ");
            String choice = scanner.nextLine();
            System.out.println();

            switch (choice) {
                case VIEW_ACCOUNTS:
                    viewAccounts(customer);
                    break;
                case CREATE_ACCOUNT:
                    createAccount(customer);
                    break;
                case DEPOSIT:
                    deposit(customer);
                    break;
                case WITHDRAW:
                    withdraw(customer);
                    break;
                case TRANSFER:
                    transfer(customer);
                    break;
                case VIEW_HISTORY:
                    viewTransactionHistory(customer);
                    break;
                case APPLY_MORTGAGE:
                    applyForMortgage(customer);
                    break;
                case VIEW_MORTGAGE:
                    viewMortgage(customer);
                    break;
                case PAY_MORTGAGE:
                    makeMortgagePayment(customer);
                    break;
                case SIGN_OUT:
                    persistence.updateCustomer(customer);
                    System.out.println("You have successfully signed out.\n");
                    return;
                default:
                    System.out.println("Invalid option. Try again.\n");
            }
        }
    }

    private static void applyForMortgage(Customer customer) {
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

    private static void viewMortgage(Customer customer) {
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

    private static void makeMortgagePayment(Customer customer) {
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

    private static void viewAccounts(Customer customer) {
        List<BankAccount> accounts = customer.getBankAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        for (BankAccount acc : accounts) {
            System.out.println("Account #" + acc.getAccountNumber() + ": Balance $" + acc.getBalance());
        }
    }

    private static void createAccount(Customer customer) {
        BankAccount newAccount = new BankAccount();
        customer.addBankAccount(newAccount);
        persistence.updateCustomer(customer);
        System.out.println("New account opened. Account #" + newAccount.getAccountNumber());
    }

    private static void deposit(Customer customer) {
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

    private static void withdraw(Customer customer) {
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

    private static void transfer(Customer customer) {
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
            // customer is transferring to another one of their accounts
            recipient = customer;
        } else {
            // customer is transferring to another customer --> fetch from DB
            // result = [Customer, BankAccount]
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
                // only update recipient in DB if transferring to another customer
                persistence.updateCustomer(recipient);
            }

            System.out.println("Successfully transferred $" + amount + " to account #" + targetAccountNumber);

        } catch (Exception e) {
            System.out.println("Failed to transfer. Please check your balance and try again.");
        }
    }

    private static void viewTransactionHistory(Customer customer) {
        BankAccount account = selectAccount(customer);
        if (account == null) {
            return;
        }

        account.viewTransactionHistory();
    }

    private static BankAccount selectAccount(Customer customer) {
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

            // cancel
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