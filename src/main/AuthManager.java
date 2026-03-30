package main;

import java.io.File;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Scanner;

public class AuthManager {
    private final String USERS_FILE;

    public AuthManager() {
        this.USERS_FILE = "data/users.csv";
    }
    public AuthManager(String USERS_FILE) {
        this.USERS_FILE = USERS_FILE;
    }

    // output:
    // true: user created successfully
    // false: user already exists or error occurred
    public boolean createUser(String username, String password) {
        try {
            if (userExists(username)) {
                return false;
            }

            String hashedPassword = hashPassword(password);

            FileWriter fw = new FileWriter(USERS_FILE, true);
            fw.write(username + "," + hashedPassword + "\n");
            fw.close();

            return true;
        } catch (Exception e) {
            System.out.println("ERROR: Unable to create the user");
            return false;
        }
    }

    // output:
    // true: authentication successful
    // false: authentication failed or error occurred
    public boolean authenticate(String username, String password) {
        try {
            String hashedPassword = hashPassword(password);

            File file = new File(USERS_FILE);
            if (!file.exists()) {
                return false;
            }

            Scanner scanner = new java.util.Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] segments = line.split(",");
                if (segments.length == 2 && segments[0].equals(username) && segments[1].equals(hashedPassword)) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
            return false;
        } catch (Exception e) {
            System.out.println("ERROR: Unable to authenticate user");
            return false;
        }
    }

    public boolean userExists(String username) {
        try {
            File file = new File(USERS_FILE);
            if (!file.exists()) {
                return false;
            }

            Scanner scanner = new java.util.Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] segments = line.split(",");
                if (segments.length == 2 && segments[0].equals(username)) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
            return false;
        } catch (Exception e) {
            System.out.println("ERROR: Unable to check if user exists");
            return false;
        }
    }

    // hashing passwords
    // reference: https://www.baeldung.com/java-password-hashing
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            System.out.println("ERROR: Unable to hash password");
            return null;
        }
    }
}
