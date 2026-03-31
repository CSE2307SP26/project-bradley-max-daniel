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
        customer = new Customer("test");
    }

    @Test
    public void testNewCustomer() {
        assertEquals(0, customer.getBankAccounts().size());
        assertEquals(null, customer.getBankAccount(0));
    }
    
    @Test
    public void testCreateBankAccount() {
        BankAccount account = new BankAccount();
        customer.addBankAccount(account);
        int accountNumber = account.getAccountNumber();
        
        assertEquals(1, customer.getBankAccounts().size());
        assertTrue(customer.getBankAccount(accountNumber) instanceof BankAccount);
        assertEquals(accountNumber, customer.getBankAccount(accountNumber).getAccountNumber());
    }

    @Test
    public void testCreateMultipleBankAccounts() {
        BankAccount account1 = new BankAccount();
        BankAccount account2 = new BankAccount();
        customer.addBankAccount(account1);
        customer.addBankAccount(account2);

        int firstAccountNumber = account1.getAccountNumber();
        int secondAccountNumber = account2.getAccountNumber();

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
        Customer customer1 = new Customer("user1");
        Customer customer2 = new Customer("user2");

        BankAccount account1 = new BankAccount();
        BankAccount account2 = new BankAccount();
        customer1.addBankAccount(account1);
        customer2.addBankAccount(account2);
        int accountNumber1 = account1.getAccountNumber();
        int accountNumber2 = account2.getAccountNumber();

        assertNotEquals(accountNumber1, accountNumber2);
    }

    @Test
    public void testGetBankAccounts() {
        customer.addBankAccount(new BankAccount());
        customer.addBankAccount(new BankAccount());

        assertEquals(2, customer.getBankAccounts().size());
        assertTrue(customer.getBankAccounts().stream().allMatch(account -> account instanceof BankAccount));
    }

    @Test
    public void testGetBankAccount(){
        BankAccount account = new BankAccount();
        customer.addBankAccount(account);
        int accountNumber = account.getAccountNumber();

        assertTrue(customer.getBankAccount(accountNumber) instanceof BankAccount);
    }

    @Test
    public void testCloseBankAccount() {
        BankAccount account = new BankAccount();
        customer.addBankAccount(account);
        assertTrue(customer.getBankAccounts().size() >= 1);

        int accountNumber = account.getAccountNumber();

        assertTrue(customer.removeAccount(accountNumber));
        assertEquals(0, customer.getBankAccounts().size());
    }

    @Test
    public void testCloseNonExistentBankAccount() {
        assertFalse(customer.removeAccount(2307));
    }

   public void testCheckAccountBalanceOfZero() {
    BankAccount account = new BankAccount();
    customer.addBankAccount(account);
    double balance = customer.checkAccountBalance(account.getAccountNumber());

    assertEquals(0,balance,.001);
   }

   @Test
   public void testCheckAccountBalance() {
    BankAccount account = new BankAccount();
    customer.addBankAccount(account);
    account.deposit(67);
    account.deposit(25);
    double balance = customer.checkAccountBalance(account.getAccountNumber());

    assertEquals(92,balance,.001);
   }

   @Test
   public void testCheckAccountBalanceNoAccount() {
       int invalidAccountNumber = 2026;
       try {
            customer.checkAccountBalance(invalidAccountNumber);
            fail();
       } catch (IllegalArgumentException exception){}
   }

   @Test
    public void testCustomerWithdraw() {
        BankAccount account = new BankAccount();
        customer.addBankAccount(account);
        account.deposit(100);
        customer.withdraw(account.getAccountNumber(), 50);
        double balance = account.getBalance();

        assertEquals(50,balance,.001);
    }

     @Test
    public void testCustomerWithdrawMoreThanBalance() {
        BankAccount account = new BankAccount();
        customer.addBankAccount(account);
        account.deposit(100);
        try{
            customer.withdraw(account.getAccountNumber(), 1000);
            fail();
        } catch (IllegalArgumentException exception){}
    }
}