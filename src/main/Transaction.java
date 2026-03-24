package main;

public class Transaction {

    private String type;
    private double amount;
    private int otherAccountNumber;

    public Transaction(String type, double amount, int otherAccountNumber) {
        this.type = type;
        this.amount = amount;
        this.otherAccountNumber = otherAccountNumber;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public int getOtherAccountNumber() {
        return otherAccountNumber;
    }

    @Override
    public String toString() {
        return type + ": $" + amount + " (Account " + otherAccountNumber + ")";
    }
}
