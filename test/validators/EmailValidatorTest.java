package validators;

import org.junit.Test;

import javax.faces.validator.ValidatorException;

import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: Giymo11
 * Date: 29.10.13
 * Time: 12:31
 * Tests if email-validator works as intended.
 */
public class EmailValidatorTest {

    private EmailValidator validator;

    @org.junit.Before
    public void setUp() throws Exception {
        validator = new EmailValidator();

    }

    @org.junit.Test
    public void testValidValidate() throws Exception {
        validator.validate(null, null, "address@host.com");
    }

    @Test
    public void testExtendedValidate() throws Exception {
        validator.validate(null, null, "firstname.lastname@test.com");
    }

    @Test
    public void testInvalidValidate() throws Exception {
        try {
            validator.validate(null, null, "omigod.thisnoemail.com");
            fail();
        } catch (ValidatorException ex) {
            // succeed()
        }
    }

    @Test
    public void testExtendedInvalidValidate() throws Exception {
        try {
            validator.validate(null, null, "omigod.thisnoemail@com");
            fail();
        } catch (ValidatorException ex) {
            // succeed()
        }
    }
}
