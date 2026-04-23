package main;

import java.util.Scanner;

public class MainMenu {
    private static Scanner scanner = new Scanner(System.in);
    private static final String CUSTOMERS_FILE = "data/customers.json";
    private static Persistence persistence = new Persistence(CUSTOMERS_FILE);
    private static AuthManager auth = new AuthManager();
    private static AuthService authService = new AuthService(auth, persistence, scanner);
    private static BankService bankService = new BankService(persistence, scanner);
    private static CustomerMenu customerMenu = new CustomerMenu(bankService, persistence, scanner);

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
        Customer customer = authService.login();
        if (customer != null) {
            customerMenu.show(customer);
        }
    }

    private static void register() {
        Customer customer = authService.register();
        if (customer != null) {
            customerMenu.show(customer);
        }
    }
}