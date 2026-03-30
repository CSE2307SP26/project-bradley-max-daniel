package main;

import java.util.Scanner;

public class MainMenu {

    private static final int CREATE_ACCOUNT = 1;
    private static final int SELECT_ACCOUNT = 2;
    private static final int DEPOSIT = 3;
    private static final int WITHDRAW = 4;
    private static final int TRANSFER = 5;
    private static final int VIEW_TRANSACTION_HISTORY = 6;
    private static final int GET_BALANCE = 7;
    private static final int CLOSE_ACCOUNT = 8;
    private static final int SIGN_OUT = 9;
    private static final int EXIT = 10;
    private static final int MAX_SELECTION = 10;

    private Customer customer;
    private String loggedInUser; // not being used yet --> will be used in my persistence PR
    private BankAccount selectedBankAccount = null;
    private Scanner keyboardInput;
    private AuthManager authManager;

    public MainMenu() {
        String loggedInUser = null;
        this.keyboardInput = new Scanner(System.in);
        this.authManager = new AuthManager();
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!\n");

        if (selectedBankAccount != null) {
            System.out.println("Selected account: #" + selectedBankAccount.getAccountNumber() + "\n");
        }

        System.out.println("1. Create a new bank account");
        System.out.println("2. Select a bank account");
        System.out.println("3. Make a deposit");
        System.out.println("4. Withdraw money");
        System.out.println("5. Transfer money");
        System.out.println("6. View transaction history");
        System.out.println("7. View balance");
        System.out.println("8. Close an existing bank account");
        System.out.println("9. Sign out");
        System.out.println("10. Exit the app");
    }

    public int getUserSelection(int max) {
        int selection = -1;
        while (selection < 1 || selection > max) {
            System.out.print("Please make a selection: ");
            selection = keyboardInput.nextInt();
            System.out.println();
        }
        return selection;
    }

    public void processInput(int selection) {
        switch (selection) {
            case CREATE_ACCOUNT:
                createAccount();
                break;
            case SELECT_ACCOUNT:
                selectAccount();
                break;
            case DEPOSIT:
                performDeposit();
                break;
            case WITHDRAW:
                performWithdraw();
                break;
            case TRANSFER:
                performTransfer();
                break;
            case VIEW_TRANSACTION_HISTORY:
                performViewTransactionHistory();
                break;
            case GET_BALANCE:
                performGetBalance();
                break;
            case CLOSE_ACCOUNT:
                closeAccount();
                break;
            case SIGN_OUT:
                signOut();
                break;
        }
    }

    public void createAccount() {
        int accountNumber = customer.createBankAccount().getAccountNumber();
        System.out.println("Successfully created a new bank account. Your new account number is: " + accountNumber);
    }

    public void selectAccount() {
        if (customer.getBankAccounts().isEmpty()) {
            System.out.println("You don't have any bank accounts. Please create one first.");
            return;
        }

        System.out.println("Your bank accounts: ");
        BankAccount[] accounts = customer.getBankAccounts().toArray(new BankAccount[0]);
        for (int i = 0; i < accounts.length; i++) {
            System.out.println((i + 1) + ". Account #" + accounts[i].getAccountNumber());
        }

        System.out.print("Select an account: ");
        int selection = getUserSelection(accounts.length);
        selectedBankAccount = accounts[selection - 1];
    }

    public void performDeposit() {
        if (selectedBankAccount == null) {
            System.out.println("No account selected. Please select an account first.");
            return;
        }

        double depositAmount = -1;
        while (depositAmount < 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = keyboardInput.nextInt();
        }
        selectedBankAccount.deposit(depositAmount);
    }

    public void closeAccount() {
        if (selectedBankAccount == null) {
            System.out.println("No account selected. Please select an account first.");
            return;
        }

        int accountNumber = selectedBankAccount.getAccountNumber();
        if (customer.closeBankAccount(accountNumber)) {
            System.out.println("Successfully closed bank account #" + accountNumber);
            selectedBankAccount = null;
        } else {
            System.out.println("Failed to close bank account #" + accountNumber);
        }
    }

    public void performTransfer() {
        if (selectedBankAccount == null) {
            System.out.println("No account selected. Please select an account first.");
            return;
        }

        System.out.print("Enter the account number you want to transfer to: ");
        int targetAccountNumber = keyboardInput.nextInt();
        BankAccount targetAccount = customer.getBankAccount(targetAccountNumber);
        if (targetAccount == null) {
            System.out.println("Target account not found. Please try again.");
            return;
        }

        double transferAmount = -1;
        while (transferAmount < 0) {
            System.out.print("How much would you like to transfer: ");
            transferAmount = keyboardInput.nextDouble();
        }

        try {
            selectedBankAccount.transfer(targetAccount, transferAmount);
            System.out.println("Successfully transferred $" + transferAmount + " to account #" + targetAccountNumber);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to transfer. Please check your balance and try again.");
        }
    }

    public void performViewTransactionHistory() {
        if (selectedBankAccount == null) {
            System.out.println("No account selected. Please select an account first.");
            return;
        }

        System.out.println("Transaction history for account #" + selectedBankAccount.getAccountNumber() + ":");
        selectedBankAccount.viewTransactionHistory();
    }

    public void performGetBalance() {
        if (selectedBankAccount == null) {
            System.out.println("No account selected. Please select an account first.");
            return;
        }

        double balance = customer.checkAccountBalance(selectedBankAccount.getAccountNumber());
        System.out.println("Current balance for account #" + selectedBankAccount.getAccountNumber() + ": $" + balance);
    }

    public void performWithdraw() {
        if (selectedBankAccount == null) {
            System.out.println("No account selected. Please select an account first.");
            return;
        }

        double withdrawAmount = -1;
        while (withdrawAmount < 0) {
            System.out.print("How much would you like to withdraw: ");
            withdrawAmount = keyboardInput.nextDouble();
        }

        try {
            customer.withdraw(selectedBankAccount.getAccountNumber(), withdrawAmount);
            System.out.println("Successfully withdrew $" + withdrawAmount + " from account #" + selectedBankAccount.getAccountNumber());
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to withdraw. Please check your balance and try again.");
        }
    }

    private void authenticateUser() {
        boolean isUserAuthenticated = false;
        int MAX_SELECTION = 3;
        final int LOGIN = 1;
        final int REGISTER = 2;
        final int AUTH_EXIT = 3;

        while (!isUserAuthenticated) {
            System.out.println("Please login or register to continue!\n");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit the app");
            System.out.println();

            int selection = getUserSelection(MAX_SELECTION);
            keyboardInput.nextLine();
            switch (selection) {
                case LOGIN:
                    isUserAuthenticated = login();
                    break;
                case REGISTER:
                    isUserAuthenticated = register();
                    break;
                case AUTH_EXIT:
                    System.exit(0);
                    break;
            }
        }
    }

    private void mainMenu() {
        int selection = -1;
        while (selection != EXIT) {
            displayOptions();
            System.out.println();
            selection = getUserSelection(MAX_SELECTION);
            processInput(selection);
            System.out.println();
        }
    }

    private boolean login() {
        System.out.println("Thanks for coming back! Please enter your account details to sign in.\n");
        System.out.print("Username: ");
        String username = keyboardInput.nextLine();
        System.out.print("Password: ");
        String password = keyboardInput.nextLine();
        
        System.out.println();
        try {
            if (authManager.authenticate(username, password)) {
                System.out.println("Login successful!\n");
                loggedInUser = username;
                return true;
            } else {
                System.out.println("Invalid username or password.\n");
            }
        } catch (Exception e) {
            System.out.println("ERROR: Unable to login. Please try again later.");
        }
        return false;
    }

    private boolean register() {
        System.out.println("Welcome! Please choose a username and password to create an account.\n");
        System.out.print("Username: ");
        String username = keyboardInput.nextLine();
        System.out.print("Password: ");
        String password = keyboardInput.nextLine();
        
        System.out.println();
        try {
            if (authManager.createUser(username, password)) {
                System.out.println("Registration successful!\n");
                loggedInUser = username;
                return true;
            } else {
                System.out.println("This username is already taken.\n");
            }
        } catch (Exception e) {
            System.out.println("ERROR: Unable to register. Please try again later.");
        }
        return false;
    }

    private void signOut() {
        System.out.println("You have successfully signed out.\n");
        loggedInUser = null;
        selectedBankAccount = null;
        authenticateUser();
        customer = new Customer();
        mainMenu();
    }

    public void run() {
        System.out.println("Welcome to the 237 Bank App!\n");
        authenticateUser();
        this.customer = new Customer();
        mainMenu();
    }
    
    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }

}