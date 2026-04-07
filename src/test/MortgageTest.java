package test;
import main.BankAccount;
import main.Customer;
import main.Mortgage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MortgageTest {
    private Mortgage mortgage;
    private Customer customer;
    
    @BeforeEach
    public void setUp() {
        mortgage = new Mortgage(10000.0,0.05, 10,10000.0);
        customer = new Customer();
    }

    @Test
    public void testApplyForMortgageForCustomerWithoutMortgage() {
        customer.applyForMortgage(customer, 10000.0,0.05,10,10000.0);

        assertTrue(customer.hasMortgage());
    }

    @Test
    public void testCustomerCannotHaveSecondMortgage() {
        customer.openMortgage(mortgage);
        Mortgage originalMortgage = mortgage;

        customer.applyForMortgage(customer, 20000.0,.05,10,20000.0);
        assertSame(originalMortgage,customer.getMortgage());

    }

    @Test
    public void testgetRemainingMortgageBalance() {
        customer.applyForMortgage(customer, 10000.0,0.05,10,10000.0);

        assertTrue(10000.0,customer.getMortgage().getRemainingBalance(), 0.00001);
    }





}
