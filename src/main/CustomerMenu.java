package main;

import java.util.Scanner;

public class CustomerMenu {
    private final BankService bankService;
    private final Persistence persistence;
    private final Scanner scanner;

    public CustomerMenu(BankService bankService, Persistence persistence, Scanner scanner) {
        this.bankService = bankService;
        this.persistence = persistence;
        this.scanner = scanner;
    }

    public void show(Customer customer) {
        final String VIEW_ACCOUNTS = "1";
        final String CREATE_ACCOUNT = "2";
        final String DEPOSIT = "3";
        final String WITHDRAW = "4";
        final String TRANSFER = "5";
        final String VIEW_HISTORY = "6";
        final String EDIT_NICKNAME = "7";
        final String APPLY_MORTGAGE = "8";
        final String VIEW_MORTGAGE = "9";
        final String PAY_MORTGAGE = "10";
        final String VIEW_CREDIT_SCORE = "11";
        final String SIGN_OUT = "12";

        while (true) {
            System.out.println("\n===============================\n");
            System.out.println("\nWelcome, " + customer.getUsername() + "!\n");
            System.out.println("1. View Accounts");
            System.out.println("2. Open New Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. View Transaction History");
            System.out.println("7. Edit Account Nickname");
            System.out.println("8. Apply for Mortgage");
            System.out.println("9. View Mortgage");
            System.out.println("10. Make Mortgage Payment");
            System.out.println("11. View Credit Score");
            System.out.println("12. Logout");

            System.out.print("\nChoose an option: ");
            String choice = scanner.nextLine();
            System.out.println();

            switch (choice) {
                case VIEW_ACCOUNTS:
                    bankService.viewAccounts(customer);
                    break;
                case CREATE_ACCOUNT:
                    bankService.createAccount(customer);
                    break;
                case DEPOSIT:
                    bankService.deposit(customer);
                    break;
                case WITHDRAW:
                    bankService.withdraw(customer);
                    break;
                case TRANSFER:
                    bankService.transfer(customer);
                    break;
                case VIEW_HISTORY:
                    bankService.viewTransactionHistory(customer);
                    break;
                case EDIT_NICKNAME:
                    bankService.editAccountNickname(customer);
                    break;
                case APPLY_MORTGAGE:
                    bankService.applyForMortgage(customer);
                    break;
                case VIEW_MORTGAGE:
                    bankService.viewMortgage(customer);
                    break;
                case PAY_MORTGAGE:
                    bankService.makeMortgagePayment(customer);
                    break;
                case VIEW_CREDIT_SCORE:
                    System.out.println("Your credit score is: " + customer.getCreditScore());
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
}
