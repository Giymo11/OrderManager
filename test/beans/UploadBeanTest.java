package beans;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Giymo11
 * Date: 18.12.13
 * Time: 14:36
 */
public class UploadBeanTest {
    @Test
    public void testHasPictureExtension() throws Exception {
        assertTrue(UploadBean.hasPictureExtension("trele.jpeg"));
        assertTrue(UploadBean.hasPictureExtension("trele.JPEG"));
        assertTrue(UploadBean.hasPictureExtension("asdkj.jpg"));
        assertTrue(UploadBean.hasPictureExtension("aFSAOjas.JPG"));
        assertTrue(UploadBean.hasPictureExtension("djkS123.png"));
        assertTrue(UploadBean.hasPictureExtension("kjsah72374t8.PNG"));

        assertTrue(UploadBean.hasPictureExtension("ljjshd.txt.jpg"));

        assertFalse(UploadBean.hasPictureExtension("omgNoJpg"));
        assertFalse(UploadBean.hasPictureExtension("fsdo123jfd.txt"));

    }
}
