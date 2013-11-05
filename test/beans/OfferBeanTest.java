package beans;

import constants.Files;
import dto.Offer;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: Giymo11
 * Date: 04.11.13
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class OfferBeanTest {

    private OfferBean bean;
    private List<File> filelist;
    private File testFile, testDirectory;

    @Before
    public void setUp() throws Exception {
        //testFile = new File(Files.getFolder() + "/test/offers.dat");
        testFile = new File(Files.OFFERSMETA.getPath());
        testDirectory = testFile.getParentFile();
        testDirectory.mkdir();

        filelist = new LinkedList<>();
        /*
        filelist.add(new File(testDirectory, "1.jpg"));
        filelist.add(new File(testDirectory, "2.jpg"));
        filelist.add(new File(testDirectory, "3.jpg"));
        filelist.add(new File(testDirectory, "1.txt"));
        filelist.add(new File(testDirectory, "2.txt"));
        filelist.add(new File(testDirectory, "3.txt"));
        */
        for (File file : filelist)
            file.createNewFile();

        bean = new OfferBean(testFile.getPath());
    }

    /*@After
    public void tearDown() throws Exception {
        for(File file : filelist)
            file.delete();

        testFile.delete();

        testDirectory.delete();
    }*/

    @Test
    public void testValidInsertOffer() throws Exception {
        bean.insertOffer(new Offer("picturepath", "textpath"), 0);
        assertEquals(new Offer("picturepath", "textpath"), bean.getOffers().get(0));
    }

    @Test
    public void testInvalidInsertOffer() throws Exception {
        try {
            bean.insertOffer(new Offer("picturepath", "textpath"), 100);
            fail();
        } catch (IndexOutOfBoundsException ex) {
            // succeed()
        }
    }

    @Test
    public void testAddOffer() throws Exception {
        bean.addOffer(new Offer("picturepath", "textpath"));
        assertEquals(new Offer("picturepath", "textpath"), bean.getOffers().get(bean.getOffers().size() - 1));
    }

    @Test
    public void testInit() throws Exception {
        bean.init();
        List<Offer> expectedList = new LinkedList<>();
        expectedList.add(new Offer("1.jpg", "1.txt"));
        expectedList.add(new Offer("2.jpg", "2.txt"));
        expectedList.add(new Offer("3.jpg", "3.txt"));
        assertEquals(expectedList, bean.getOffers());
    }
}
