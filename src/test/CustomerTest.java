package test;
import main.BankAccount;
import main.Customer;
import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//imported so fail works in test
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
    
    //Withdraw tests

    @Test
    public void testCustomerWithdraw() {
        BankAccount account = customer.createBankAccount();

        account.deposit(100);

        customer.withdraw(account.getAccountNumber(),50);

        double balance = account.getBalance();

        assertEquals(50,balance,.001);
    }

    @Test
    public void testCustomerWithdrawMoreThanBalance() {
        BankAccount account = customer.createBankAccount();

        account.deposit(100);

        try{
            customer.withdraw(account.getAccountNumber(), 1000);
            fail();
        } catch (IllegalArgumentException exception){

        }
    }




}