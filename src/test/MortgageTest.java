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

}

