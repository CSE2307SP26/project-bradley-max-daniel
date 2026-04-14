package main;

import java.util.Scanner;

public class AuthService {
    private final AuthManager authManager;
    private final Persistence persistence;
    private final Scanner scanner;

    public AuthService(AuthManager authManager, Persistence persistence, Scanner scanner) {
        this.authManager = authManager;
        this.persistence = persistence;
        this.scanner = scanner;
    }

    public Customer login() {
        System.out.println("Thanks for coming back! Please enter your account details to sign in.\n");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.println();

        try {
            if (!authManager.authenticate(username, password)) {
                System.out.println("Invalid username or password.\n");
                return null;
            }

            Customer customer = persistence.getCustomer(username);
            if (customer == null) {
                customer = new Customer(username);
                persistence.updateCustomer(customer);
            }
            return customer;
        } catch (Exception e) {
            System.out.println("ERROR: Unable to log in. Please try again later.");
            return null;
        }
    }

    public Customer register() {
        System.out.println("Welcome! Please choose a username and password to create an account.\n");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.println();

        try {
            if (!authManager.createUser(username, password)) {
                System.out.println("This username is already taken.\n");
                return null;
            }

            Customer customer = new Customer(username);
            persistence.updateCustomer(customer);
            System.out.println("Registration successful!\n");
            return customer;
        } catch (Exception e) {
            System.out.println("ERROR: Unable to register. Please try again later.");
            return null;
        }
    }
}
