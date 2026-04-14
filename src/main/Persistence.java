package main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.reflect.Type;

public class Persistence {
    private final Gson gson = new Gson();
    private String CUSTOMERS_FILE = "data/customers.json";
    private List<Customer> customers;

    public Persistence(String customersFilePath) {
        this.CUSTOMERS_FILE = customersFilePath;
        load();
    }

    // Loads all customers from the DB (customers.json) into temp storage (buffer)
    public final void load() {
        this.customers = new ArrayList<>();
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) {
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            StringBuilder json = new StringBuilder();
            while (scanner.hasNextLine()) {
                json.append(scanner.nextLine());
            }

            Type listType = new TypeToken<ArrayList<Customer>>(){}.getType();
            List<Customer> loaded = gson.fromJson(json.toString(), listType);
            if (loaded != null) {
                this.customers = loaded;
            }
        } catch (Exception e) {
            System.out.println("ERROR: Unable to load customers from " + CUSTOMERS_FILE);
        }
    }

    // Updates the temporary buffer
    public void save() {
        try {
            FileWriter writer = new FileWriter(CUSTOMERS_FILE, false);
            gson.toJson(this.customers, writer);
            writer.close();
        } catch (Exception e) {
            System.out.println("ERROR: Unable to save customers from " + CUSTOMERS_FILE);
        }
    }

    public List<Customer> getAllCustomers() {
        return this.customers;
    }

    public Customer getCustomer(String username) {
        for (Customer customer : this.customers) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }
        
        return null;
    }

    // Updates or adds a customer (if not found) to the buffer
    public void updateCustomer(Customer customer) {
        boolean found = false;
        for (int i = 0; i < this.customers.size(); i++) {
            if (this.customers.get(i).getUsername().equals(customer.getUsername())) {
                this.customers.set(i, customer);
                found = true;
                break;
            }
        }
        
        if (!found) {
            this.customers.add(customer);
        }
    }

    // Returns [Customer, BankAccount] if found
    public Object[] findBankAccount(int accountNumber) {
        for (Customer customer : this.customers) {
            for (BankAccount account : customer.getBankAccounts()) {
                if (account.getAccountNumber() == accountNumber) {
                    return new Object[]{customer, account};
                }
            }
        }

        return null;
    }
}
