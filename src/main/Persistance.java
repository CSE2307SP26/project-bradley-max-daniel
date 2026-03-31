package main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.reflect.Type;

public class Persistance {
    private static final Gson gson = new Gson();
    private static String CUSTOMERS_FILE = "data/customers.json";

    // PersistanceTest
    public static void setFilePath(String filePath) {
        CUSTOMERS_FILE = filePath;
    }

    // loads all customers from the DB (customers.json)
    public static List<Customer> loadCustomersDB() {
        List<Customer> customers = new ArrayList<>();
        File file = new File(CUSTOMERS_FILE);

        if (!file.exists()) { 
            return customers;
        }

        try (Scanner scanner = new Scanner(file)) {
            StringBuilder json = new StringBuilder();
            while (scanner.hasNextLine()) {
                json.append(scanner.nextLine());
            }

            Type listType = new TypeToken<ArrayList<Customer>>(){}.getType();
            customers = gson.fromJson(json.toString(), listType);

            if (customers == null) { 
                customers = new ArrayList<>();
            }
        } catch (Exception e) {
            System.out.println("ERROR: Unable to load customers from data/customers.json");
        }
        return customers;
    }

    // updates the entire DB
    // we need to update the entire DB at once since there's no real way to-
    // track real-time updates via files
    public static void updateDB(List<Customer> customers) {
        try {
            FileWriter writer = new FileWriter(CUSTOMERS_FILE, false);
            gson.toJson(customers, writer);
            writer.close();
        } catch (Exception e) {
            System.out.println("ERROR: Unable to save customers");
        }
    }

    public static Customer getCustomer(String username) {
        List<Customer> customers = loadCustomersDB();
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }
        
        return null;
    }

    // updates or adds a customer (if not found) to the DB
    public static void updateCustomer(Customer customer) {
        List<Customer> customers = loadCustomersDB();

        boolean found = false;
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getUsername().equals(customer.getUsername())) {
                customers.set(i, customer);
                found = true;
                break;
            }
        }

        if (!found) { 
            customers.add(customer); 
        }

        updateDB(customers);
    }

    // returns [Customer, BankAccount]
    public static Object[] findBankAccount(int accountNumber) {
        List<Customer> customers = loadCustomersDB();

        for (Customer customer : customers) {
            for (BankAccount account : customer.getBankAccounts()) {
                if (account.getAccountNumber() == accountNumber) {
                    return new Object[]{customer, account};
                }
            }
        }

        return null;
    }
}
