package main;

public class Transaction {
    private String type;
    private double amount;
    private int accountNumber;
    private Integer otherAccountNumber; // transfers

    public Transaction(String type, double amount, int accountNumber) {
        this.type = type;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.otherAccountNumber = null;
    }

    // transfers
    public Transaction(String type, double amount, int accountNumber, int otherAccountNumber) {
        this.type = type;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.otherAccountNumber = otherAccountNumber;
    }

    public String getType() { 
        return type;
    }

    public double getAmount() { 
        return amount;
    }

    public int getAccountNumber() { 
        return accountNumber;
    }
    public Integer getOtherAccountNumber() { 
        return otherAccountNumber;
    }

    @Override
    public String toString() {
        if (otherAccountNumber != null) {
            return type + ": $" + amount + " (Account " + accountNumber + ", Other Account " + otherAccountNumber + ")";
        } else {
            return type + ": $" + amount + " (Account " + accountNumber + ")";
        }
    }
}
