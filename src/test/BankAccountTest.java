package test;
import main.BankAccount;
import main.Transaction;
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
public void testViewTransactionHistoryIndirectBothAccounts() {
    sender.deposit(100);
    sender.transfer(receiver, 50);

    // sender history
    assertEquals(1, sender.getTransactionHistory().size()); //confirming only 1 transaction in sender history
    Transaction senderTransaction = sender.getTransactionHistory().get(0);

    String expectedSender = "Transfer Out: $50.0 (Account " + receiver.getAccountNumber() + ")";
    assertEquals(expectedSender, senderTransaction.toString());

    // receiver history
    assertEquals(1, receiver.getTransactionHistory().size()); //confirming only 1 transaction in receiver history
    Transaction receiverTransaction = receiver.getTransactionHistory().get(0);

    String expectedReceiver = "Transfer In: $50.0 (Account " + sender.getAccountNumber() + ")";
    assertEquals(expectedReceiver, receiverTransaction.toString());
}

}