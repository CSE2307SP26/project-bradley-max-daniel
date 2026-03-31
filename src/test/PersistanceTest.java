package test;

import main.Persistance;
import main.Customer;
import main.BankAccount;
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class PersistanceTest {
	private static final String TEST_FILE = "src/test/data/customers.json";

    // customers.json can get very large, so for each test we create and destroy it
	@BeforeEach
	public void setUp() {
		Persistance.setFilePath(TEST_FILE);
		File file = new File(TEST_FILE);
		if (file.exists()) { 
            file.delete(); 
        }
	}

	@AfterEach
	public void tearDown() {
		File file = new File(TEST_FILE);
		if (file.exists()) { 
            file.delete();
        }
	}

    @Test
    public void testLoadEmptyDB() {
        List<Customer> customers = Persistance.loadCustomersDB();

        assertTrue(customers.isEmpty());
    }

    @Test
    public void testSaveCustomer() {
        Customer customer = new Customer("test");
        Persistance.updateDB(Arrays.asList(customer));

        List<Customer> DB = Persistance.loadCustomersDB();
        assertEquals(1, DB.size());
        assertEquals(customer.getUsername(), Persistance.getCustomer("test").getUsername());
        assertEquals("test", DB.get(0).getUsername());
    }

    @Test
	public void testUpdateCustomer() {
		Customer customer = new Customer("test");
		customer.addBankAccount(new BankAccount());
		Persistance.updateDB(Arrays.asList(customer));

		Customer DB_Customer = Persistance.getCustomer("test");
		assertNotNull(DB_Customer);
		assertEquals(1, DB_Customer.getBankAccounts().size());
		assertEquals(0.0, DB_Customer.getBankAccounts().get(0).getBalance());

		DB_Customer.getBankAccounts().get(0).deposit(500);
		Persistance.updateCustomer(DB_Customer);

		DB_Customer = Persistance.getCustomer("test");
		assertNotNull(DB_Customer);
		assertEquals(1, DB_Customer.getBankAccounts().size());
		assertEquals(500.0, DB_Customer.getBankAccounts().get(0).getBalance());
	}

    	@Test
	public void testGetCustomer() {
		Customer customer = new Customer("test");
		customer.addBankAccount(new BankAccount());
		Persistance.updateDB(Arrays.asList(customer));

		Customer found = Persistance.getCustomer("test");
		
        assertNotNull(found);
		assertEquals("test", found.getUsername());
		assertEquals(1, found.getBankAccounts().size());

		Customer notFound = Persistance.getCustomer("Daniel");
		
        assertNull(notFound);
	}

	@Test
	public void testFindBankAccount() {
		Customer test1 = new Customer("test 1");
		BankAccount account1 = new BankAccount();
		account1.deposit(100);
		test1.addBankAccount(account1);

		Persistance.updateDB(Arrays.asList(test1));
        Object[] result1 = Persistance.findBankAccount(account1.getAccountNumber());
		
        assertNotNull(result1);
        
		Customer c1 = (Customer) result1[0];
		BankAccount ba1 = (BankAccount) result1[1];
		
        assertEquals("test 1", c1.getUsername());
		assertEquals(account1.getAccountNumber(), ba1.getAccountNumber());
		assertEquals(100.0, ba1.getBalance());

		Object[] result2 = Persistance.findBankAccount(2307);
		assertNull(result2);
	}
}
