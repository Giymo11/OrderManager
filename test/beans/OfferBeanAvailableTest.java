package beans;

import constants.Files;
import dto.Offer;
import org.junit.After;
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
 * Tests if files for sample special offers are in place.
 */
public class OfferBeanAvailableTest {

    private OfferBean bean;
    private File testFile;

    @Before
    public void setUp() throws Exception {

        testFile = new File(Files.OFFERSMETA.getPath());
        File testDirectory = testFile.getParentFile();
        testDirectory.mkdir();

        bean = new OfferBean(testFile.getPath());
    }

    @After
    public void tearDown() throws Exception {
        testFile.delete();
    }

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
        expectedList.add(new Offer(Files.OFFERSDIR.getPath() + "1.jpg", Files.OFFERSDIR.getPath() + "1.txt"));
        expectedList.add(new Offer(Files.OFFERSDIR.getPath() + "2.jpg", Files.OFFERSDIR.getPath() + "2.txt"));
        expectedList.add(new Offer(Files.OFFERSDIR.getPath() + "3.jpg", Files.OFFERSDIR.getPath() + "3.txt"));
        assertEquals(expectedList, bean.getOffers());
    }
}
