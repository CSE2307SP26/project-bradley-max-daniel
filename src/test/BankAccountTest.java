package test;
import main.BankAccount;
import main.Transaction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

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
    sender.deposit(100);    // sender: deposit transaction
    sender.transfer(receiver, 50); // sender: transfer out transaction, receiver: transfer in transaction

    // sender history
    assertEquals(2, sender.getTransactionHistory().size());
    Transaction senderTransaction = sender.getTransactionHistory().get(1);
    String expectedSender = "Transfer Out: $50.0 (Account " + receiver.getAccountNumber() + ")";
    assertEquals(expectedSender, senderTransaction.toString());

    // receiver history
    assertEquals(1, receiver.getTransactionHistory().size()); //confirming only 1 transaction in receiver history
    Transaction receiverTransaction = receiver.getTransactionHistory().get(0);

    String expectedReceiver = "Transfer In: $50.0 (Account " + sender.getAccountNumber() + ")";
    assertEquals(expectedReceiver, receiverTransaction.toString());
}

    @Test
    public void testTransferValidAmount() {
        sender.deposit(100.0);
        sender.transfer(receiver, 40.0);

        assertEquals(60.0, sender.getBalance(), 0.0001); //the 0.0001 is the delta for floating point comparison
        assertEquals(40.0, receiver.getBalance(), 0.0001);
    }

    @Test
    public void testTransferZeroAmount() {
        sender.deposit(100.0);

        assertThrows(IllegalArgumentException.class, () -> { 
            sender.transfer(receiver, 0.0);
        });

        //source for assertThrows: https://docs.junit.org/5.0.1/api/org/junit/jupiter/api/Assertions.html
    }

    @Test
    public void testTransferNegativeAmount() {
        sender.deposit(100.0);

        assertThrows(IllegalArgumentException.class, () -> {
            sender.transfer(receiver, -25.0);
        });
    }

    @Test
    public void testTransferMoreThanBalance() {
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

     @Test
    public void testDeposit() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        assertEquals(50, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInvalidDeposit() {
        BankAccount testAccount = new BankAccount();
        try {
            testAccount.deposit(-50);
            fail();
        } catch (IllegalArgumentException e) {
            //do nothing, test passes
        }
    }

    @Test
    public void testWithdraw() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        testAccount.withdraw(20);
        
        double testAccountBalance = testAccount.getBalance();

        assertEquals(30, testAccountBalance, .001);
    }


    @Test
    public void testWithdrawMoreThanBalance() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        
        try {
            testAccount.withdraw(200);
            fail();
        } catch (IllegalArgumentException e) {
            //do nothing, test passes
        }
    }

    @Test
    public void testSetAndGetNickname() {
        BankAccount acc = new BankAccount();

        assertNull(acc.getNickname());

        
        acc.setNickname("Emergency Fund");
        assertEquals("Emergency Fund", acc.getNickname());
    }


}