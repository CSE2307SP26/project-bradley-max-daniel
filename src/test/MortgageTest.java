package test;


import main.Customer;
import main.Mortgage;


import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class MortgageTest {
   private Mortgage mortgage;
   private Customer customer;


   @BeforeEach
   public void setUp() {
       mortgage = new Mortgage(10000.0, 0.05, 10);
       customer = new Customer("max");
   }


   @Test
   public void testApplyForMortgageForCustomerWithoutMortgage() {
       customer.applyForMortgage(10000.0, 0.05, 10);
       assertTrue(customer.hasMortgage());
   }


   @Test
   public void testCustomerCannotHaveSecondMortgage() {
       customer.applyForMortgage(10000.0, 0.05, 10);
       Mortgage originalMortgage = customer.getMortgage();


       assertThrows(IllegalStateException.class, () -> {
           customer.applyForMortgage(20000.0, 0.05, 10);
       });


       assertSame(originalMortgage, customer.getMortgage());
   }

   @Test
   public void testGetRemainingMortgageBalance() {
       customer.applyForMortgage(10000.0, 0.05, 10);
       assertEquals(10000.0, customer.getMortgage().getRemainingBalance(), 0.00001);
   }

   @Test
    public void testMakePaymentReducesRemainingBalance() {
        mortgage.makePayment(2500.0);
        assertEquals(7500.0, mortgage.getRemainingBalance(), 0.00001);
    }

    @Test
    public void testMakePaymentEqualToRemainingPaysOffMortgage() {
        mortgage.makePayment(10000.0);
        assertEquals(0.0, mortgage.getRemainingBalance(), 0.00001);
    }

    @Test
    public void testMakePaymentGreaterThanRemainingSetsBalanceToZero() {
        mortgage.makePayment(12000.0);
        assertEquals(0.0, mortgage.getRemainingBalance(), 0.00001);
    }

    @Test
    public void testMakePaymentWithZeroThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> mortgage.makePayment(0.0));
    }

    @Test
    public void testMakePaymentWithNegativeAmountThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> mortgage.makePayment(-100.0));
    }

    @Test
    public void testIsPaidOffFalseWhenBalanceRemains() {
        mortgage.makePayment(3000.0);
        assertFalse(mortgage.isPaidOff());
    }

    @Test
    public void testIsPaidOffTrueWhenBalanceIsZero() {
        mortgage.makePayment(10000.0);
        assertTrue(mortgage.isPaidOff());
    }



}

