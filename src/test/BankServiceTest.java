package test;

import main.*;
import org.junit.jupiter.api.*;
import java.util.*;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

// tests the methods in BankService that aren't tested in the other domain objects (Customer, BankAccount, AuthManager, Mortgage, etc.)
public class BankServiceTest {
    private Persistence persistence;
    private BankService bankService;
    private Customer customer;
    private static final String CUSTOMERS_FILE = "src/test/data/customers.json";

    @BeforeEach
    public void setUp() {
        persistence = new Persistence(CUSTOMERS_FILE);
        bankService = new BankService(persistence, new Scanner(System.in));
        customer = new Customer("testuser");
        persistence.updateCustomer(customer);
    }

    @AfterEach
    public void tearDown() {
        File file = new File(CUSTOMERS_FILE);
        if (file.exists()) {
			file.delete();
		}
    }

    @Test
    public void testCreateAccount() {
        bankService.createAccount(customer);
        assertEquals(1, customer.getBankAccounts().size());
    }

    @Test
    public void testDepositAndWithdraw() {
        bankService.createAccount(customer);
        BankAccount acc = customer.getBankAccounts().get(0);
        acc.deposit(100);
        assertEquals(100, acc.getBalance());
        acc.withdraw(50);
        assertEquals(50, acc.getBalance());
    }

    @Test
    public void testApplyForMortgage() {
        assertFalse(customer.hasMortgage());
        customer.applyForMortgage(1000, 10);
        assertTrue(customer.hasMortgage());
        assertEquals(1000, customer.getMortgage().getLoanAmount());
    }

    @Test
    public void testTransferBetweenAccounts() {
        bankService.createAccount(customer);
        bankService.createAccount(customer);
        BankAccount acc1 = customer.getBankAccounts().get(0);
        BankAccount acc2 = customer.getBankAccounts().get(1);
        acc1.deposit(200);
        acc1.transfer(acc2, 100);
        assertEquals(100, acc1.getBalance());
        assertEquals(100, acc2.getBalance());
    }
}
