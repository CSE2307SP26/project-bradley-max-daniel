package main;

import java.util.Scanner;

public class MainMenu {

    private static final int CREATE_ACCOUNT = 1;
    private static final int SELECT_ACCOUNT = 2;
    private static final int DEPOSIT = 3;
    private static final int CLOSE_ACCOUNT = 4;
    private static final int EXIT = 5;
	private static final int MAX_SELECTION = 5;

    private Customer customer;
    private BankAccount selectedBankAccount = null;
    private Scanner keyboardInput;

    public MainMenu() {
        this.customer = new Customer();
        this.keyboardInput = new Scanner(System.in);
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!\n");

        if (selectedBankAccount != null) {
            System.out.println("Selected account: #" + selectedBankAccount.getAccountNumber() + "\n");
        }
        
        System.out.println("1. Create a new bank account");
        System.out.println("2. Select a bank account");
        System.out.println("3. Make a deposit");
        System.out.println("4. Close an existing bank account");
        System.out.println("5. Exit the app");
    }

    public int getUserSelection(int max) {
        int selection = -1;
        while(selection < 1 || selection > max) {
            System.out.print("Please make a selection: ");
            selection = keyboardInput.nextInt();
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
            case CLOSE_ACCOUNT:
                closeAccount();
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
        while(depositAmount < 0) {
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

    public void run() {
        int selection = -1;
        while(selection != EXIT) {
            displayOptions();
            System.out.println();
            selection = getUserSelection(MAX_SELECTION);
            processInput(selection);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }

}
