package test;

import main.AuthManager;
import org.junit.jupiter.api.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthManagerTest {
    private static final String TEST_USER_FILE = "src/test/data/users.csv";
    private AuthManager authManager;

    @BeforeEach
    public void setup() {
        authManager = new AuthManager(TEST_USER_FILE);
    }

    @Test
    public void usersFileCreatedIfNotExists() {
        File f = new File(TEST_USER_FILE);
        if (f.exists()) { 
            f.delete();
        }

        assertTrue(authManager.createUser("create file", "1234567890"));
        assertTrue(f.exists());
        f.delete();
    }

    @Test
    public void testUserExists() {
        assertFalse(authManager.userExists("test user"));
        authManager.createUser("test user", "123");
        assertTrue(authManager.userExists("test user"));
    }

    @Test
    public void testRegisterUniqueUsername() {
        assertTrue(authManager.createUser("unique", "12345"));
        assertTrue(authManager.userExists("unique"));
    }

    @Test
    public void testRegisterNotUniqueUsername() {
        assertTrue(authManager.createUser("existing user", "123"));
        assertFalse(authManager.createUser("existing user", "123"));
    }

    @Test
    public void testLoginSuccess() {
        assertTrue(authManager.createUser("user", "cse 2307"));
        assertTrue(authManager.authenticate("user", "cse 2307"));
    }

    @Test
    public void testLoginFail() {
        assertTrue(authManager.createUser("user1", "pass"));
        assertFalse(authManager.authenticate("user1", "I forgot"));
    }

    @Test
    public void testLoginCaseSensitive() {
        assertTrue(authManager.createUser("userName", "AbC"));
        assertFalse(authManager.authenticate("username", "AbC")); // username is case-sensitive
        assertFalse(authManager.authenticate("userName", "abc")); // password is case-sensitive
        assertTrue(authManager.authenticate("userName", "AbC"));
    }
}
