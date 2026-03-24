package test;
import main.BankAccount;
import main.Customer;
import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

public class CustomerTest {
    private Customer customer;


    @BeforeEach
    public void setUp() {
        customer = new Customer();
    }

    //New check balance tests
   @Test
   public void testCheckAccountBalanceOfZero() {
       BankAccount account = customer.createBankAccount();


       double balance = customer.checkAccountBalance(account.getAccountNumber());


       assertEquals(0,balance,.001);
   }


   @Test
   public void testCheckAccountBalance() {
       BankAccount account = customer.createBankAccount();




       account.deposit(67);
       account.deposit(25);


       double balance = customer.checkAccountBalance(account.getAccountNumber());


       assertEquals(92,balance,.001);
   }
   @Test
   public void testCheckAccountBalanceNoAccount() {
       int invalidAccountNumber = 2026;


       try{
           customer.checkAccountBalance(invalidAccountNumber);
           fail();
       } catch (IllegalArgumentException exception){


       }
   }

}