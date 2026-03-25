package test;
import main.BankAccount;
import main.Customer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerTest {
    private Customer customer;


    @BeforeEach
    public void setUp() {
        customer = new Customer();
    }

    @Test
    public void testNewCustomer() {
        assertEquals(0, customer.getBankAccounts().size());
        assertEquals(null, customer.getBankAccount(0));
    }
    
    @Test
    public void testCreateBankAccount() {
        customer.createBankAccount();

        BankAccount[] bankAccounts = customer.getBankAccounts().toArray(new BankAccount[0]);
        int accountNumber = bankAccounts[0].getAccountNumber();
        
        assertEquals(1, customer.getBankAccounts().size());
        assertTrue(customer.getBankAccount(accountNumber) instanceof BankAccount);
        assertEquals(accountNumber, customer.getBankAccount(accountNumber).getAccountNumber());
    }

    @Test
    public void testCreateMultipleBankAccounts() {
        customer.createBankAccount();
        customer.createBankAccount();

        BankAccount[] bankAccounts = customer.getBankAccounts().toArray(new BankAccount[0]);
        int firstAccountNumber = bankAccounts[0].getAccountNumber();
        int secondAccountNumber = bankAccounts[1].getAccountNumber();
        
        
        assertEquals(2, customer.getBankAccounts().size());
        assertTrue(customer.getBankAccount(firstAccountNumber) instanceof BankAccount);
        assertTrue(customer.getBankAccount(secondAccountNumber) instanceof BankAccount);
        assertEquals(firstAccountNumber, customer.getBankAccount(firstAccountNumber).getAccountNumber());
        assertEquals(secondAccountNumber, customer.getBankAccount(secondAccountNumber).getAccountNumber());
    }

    @Test
    public void testGetNonExistentBankAccount() {
        assertEquals(null, customer.getBankAccount(2307));
    }

    @Test
    public void testMultipleCustomersUniqueAccountNumbers() {
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();

        customer1.createBankAccount();
        customer2.createBankAccount();

        int accountNumber1 = customer1.getBankAccounts().iterator().next().getAccountNumber();
        int accountNumber2 = customer2.getBankAccounts().iterator().next().getAccountNumber();

        assertNotEquals(accountNumber1, accountNumber2);
    }

    @Test
    public void testGetBankAccounts() {
        customer.createBankAccount();
        customer.createBankAccount();

        assertEquals(2, customer.getBankAccounts().size());
        assertTrue(customer.getBankAccounts().stream().allMatch(account -> account instanceof BankAccount));
    }

    @Test
    public void testGetBankAccount(){
        customer.createBankAccount();
        int accountNumber = customer.getBankAccounts().iterator().next().getAccountNumber();
        assertTrue(customer.getBankAccount(accountNumber) instanceof BankAccount);
    }

    @Test
    public void testCloseBankAccount() {
        customer.createBankAccount();
        assertTrue(customer.getBankAccounts().size() >= 1);

        int accountNumber = customer.getBankAccounts().iterator().next().getAccountNumber();

        assertTrue(customer.closeBankAccount(accountNumber));
        assertEquals(0, customer.getBankAccounts().size());
    }

    @Test
    public void testCloseNonExistentBankAccount() {
        assertFalse(customer.closeBankAccount(2307));
    }

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