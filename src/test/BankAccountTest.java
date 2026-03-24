package test;
import main.BankAccount;
import main.Customer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankAccountTest {

    private BankAccount sender;
    private BankAccount receiver;

    @BeforeEach
    public void setUp() {
        sender = new BankAccount();
        receiver = new BankAccount();
    }

    @Test
    public void testTransferValidAmount() {
        sender.deposit(100.0);
        sender.transfer(receiver, 40.0);

        assertEquals(60.0, sender.getBalance(), 0.0001);
        assertEquals(40.0, receiver.getBalance(), 0.0001);
    }

    @Test
    public void testTransferZeroAmountThrowsException() {
        sender.deposit(100.0);

        assertThrows(IllegalArgumentException.class, () -> {
            sender.transfer(receiver, 0.0);
        });
    }

    @Test
    public void testTransferNegativeAmountThrowsException() {
        sender.deposit(100.0);

        assertThrows(IllegalArgumentException.class, () -> {
            sender.transfer(receiver, -25.0);
        });
    }

    @Test
    public void testTransferMoreThanBalanceThrowsException() {
        sender.deposit(50.0);

        assertThrows(IllegalArgumentException.class, () -> {
            sender.transfer(receiver, 75.0);
        });
    }

    @Test
    public void testTransferExactBalance() {
        sender.deposit(80.0);
        sender.transfer(receiver, 80.0);

        assertEquals(0.0, sender.getBalance(), 0.0001);
        assertEquals(80.0, receiver.getBalance(), 0.0001);
    }
}