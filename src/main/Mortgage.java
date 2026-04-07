package main;

public class Mortgage {
    private double loanAmount;
    private double annualRate;
    private double termYears;
    private double remainingAmount;

    public Mortgage(double loanAmount, double annualRate, double termYears) {
        this.loanAmount = loanAmount;
        this.annualRate = annualRate;
        this.termYears = termYears;
        this.remainingAmount = loanAmount;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getAnnualRate() {
        return annualRate;
    }

    public double getTermYears() {
        return termYears;
    }

    public double getRemainingBalance() {
        return remainingAmount;
    }

    public void makePayment(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment must be greater than 0");
        }

        remainingAmount = Math.max(0, remainingAmount - amount);
    }

    public boolean isPaidOff() {
        return remainingAmount == 0;
    }
}