package test;

import main.Persistence;
import main.Customer;
import main.BankAccount;
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class PersistenceTest {
	private final String TEST_FILE = "src/test/data/customers.json";
	private Persistence persistence;

    // customers.json can get very large, so for each test we create and destroy it
	@BeforeEach
	public void setUp() {
		persistence = new Persistence(TEST_FILE);
		persistence.load();

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
		persistence.load();
		List<Customer> customers = persistence.getAllCustomers();
		assertTrue(customers.isEmpty());
	}

	@Test
	public void testSaveCustomer() {
		Customer customer = new Customer("test");
		persistence.updateCustomer(customer);
		persistence.save();
		persistence.load();
		List<Customer> DB = persistence.getAllCustomers();
		assertEquals(1, DB.size());
		assertEquals(customer.getUsername(), persistence.getCustomer("test").getUsername());
		assertEquals("test", DB.get(0).getUsername());
	}

	@Test
	public void testUpdateCustomer() {
		Customer customer = new Customer("test");
		customer.addBankAccount(new BankAccount());
		persistence.updateCustomer(customer);
		persistence.save();
		persistence.load();
		Customer DB_Customer = persistence.getCustomer("test");
		assertNotNull(DB_Customer);
		assertEquals(1, DB_Customer.getBankAccounts().size());
		assertEquals(0.0, DB_Customer.getBankAccounts().get(0).getBalance());

		DB_Customer.getBankAccounts().get(0).deposit(500);
		persistence.updateCustomer(DB_Customer);
		persistence.save();
		persistence.load();
		DB_Customer = persistence.getCustomer("test");
		assertNotNull(DB_Customer);
		assertEquals(1, DB_Customer.getBankAccounts().size());
		assertEquals(500.0, DB_Customer.getBankAccounts().get(0).getBalance());
	}

	@Test
	public void testGetCustomer() {
		Customer customer = new Customer("test");
		customer.addBankAccount(new BankAccount());
		persistence.updateCustomer(customer);
		persistence.save();
		persistence.load();
		Customer found = persistence.getCustomer("test");
		assertNotNull(found);
		assertEquals("test", found.getUsername());
		assertEquals(1, found.getBankAccounts().size());
		Customer notFound = persistence.getCustomer("Daniel");
		assertNull(notFound);
	}

	@Test
	public void testFindBankAccount() {
		Customer test1 = new Customer("test 1");
		BankAccount account1 = new BankAccount();
		account1.deposit(100);
		test1.addBankAccount(account1);
		persistence.updateCustomer(test1);
		persistence.save();
		persistence.load();
		Object[] result1 = persistence.findBankAccount(account1.getAccountNumber());
		assertNotNull(result1);
		Customer c1 = (Customer) result1[0];
		BankAccount ba1 = (BankAccount) result1[1];
		assertEquals("test 1", c1.getUsername());
		assertEquals(account1.getAccountNumber(), ba1.getAccountNumber());
		assertEquals(100.0, ba1.getBalance());
		Object[] result2 = persistence.findBankAccount(2307);
		assertNull(result2);
	}
}
