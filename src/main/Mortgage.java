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


}

