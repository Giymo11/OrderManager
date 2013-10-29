package constants;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Giymo11
 * Date: 29.10.13
 * Time: 13:52
 * Tests if necessary files are available.
 */
public class FilesAvailableTest {
    @Test
    public void testGetInfoPath() throws Exception {
        File file = new File(Files.INFO.getPath());
        assertTrue(file.exists());
    }

    @Test
    public void testGetAdminPath() throws Exception {
        File file = new File(Files.ADMIN.getPath());
        assertTrue(file.exists());
    }
}
