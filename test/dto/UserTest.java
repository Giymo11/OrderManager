package dto;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Giymo11
 * Date: 29.10.13
 * Time: 13:56
 * Tests User dto logic (equals)
 */
public class UserTest {
    @Test
    public void testEquals() throws Exception {
        User user1 = new User("email@host.com", "trele");
        User user2 = new User("email@host.com", "trele");
        assertTrue(user1.equals(user2));
    }
}
