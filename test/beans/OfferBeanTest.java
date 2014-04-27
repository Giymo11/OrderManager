package beans;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Giymo11
 * Date: 04.11.13
 * Time: 14:49
 */
public class OfferBeanTest {

    private OfferService bean;
    private List<File> filelist;
    private File testFile, testDirectory;

    @Before
    public void setUp() throws Exception {
        testFile = new File(/*Files.getFolder()*/ "" + "\\test\\offers.dat");
        testDirectory = testFile.getParentFile();
        testDirectory.mkdir();

        filelist = new LinkedList();

        filelist.add(new File(testDirectory, "1.jpg"));
        filelist.add(new File(testDirectory, "2.jpg"));
        filelist.add(new File(testDirectory, "3.jpg"));
        filelist.add(new File(testDirectory, "1.txt"));
        filelist.add(new File(testDirectory, "2.txt"));
        filelist.add(new File(testDirectory, "3.txt"));

        for (File file : filelist)
            file.createNewFile();

        bean = new OfferService();
    }

    @After
    public void tearDown() throws Exception {
        for (File file : filelist)
            file.delete();

        testFile.delete();

        testDirectory.delete();
    }

    @Test
    public void testValidInsertOffer() throws Exception {

    }

    @Test
    public void testInvalidInsertOffer() throws Exception {

    }

    @Test
    public void testAddOffer() throws Exception {

    }
}
