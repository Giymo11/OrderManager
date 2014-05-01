package beans;

import dao.AboutDao;
import dao.PictureDao;
import dto.About;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 17.02.14
 * Time: 14:16
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ApplicationScoped
public class AboutService {
    private AboutDao aboutDao;
    private PictureDao pictureDao;
    private List<About> aboutList;

    public AboutService() {
        aboutDao = new AboutDao();
        pictureDao = new PictureDao();
    }

    public About getFirst() {
        if (aboutList == null) {
            aboutList = aboutDao.getAboutList();
        }
        return aboutList.get(0);
    }

    public About getSecond() {
        if (aboutList == null) {
            aboutList = aboutDao.getAboutList();
        }

        return aboutList.get(1);
    }

    public About getThird() {
        if (aboutList == null) {
            aboutList = aboutDao.getAboutList();
        }
        return aboutList.get(2);
    }

    public String save(){
        aboutDao.save(aboutList);
        return "#";
    }

    public String getFirstPicture() {
        if (aboutList == null)
            aboutList = aboutDao.getAboutList();
        return pictureDao.getNameForID(aboutList.get(0).getPictureID());
    }

    public String getSecondPicture() {
        if (aboutList == null)
            aboutList = aboutDao.getAboutList();
        return pictureDao.getNameForID(aboutList.get(1).getPictureID());
    }

    public String getThirdPicture() {
        if (aboutList == null)
            aboutList = aboutDao.getAboutList();
        return pictureDao.getNameForID(aboutList.get(2).getPictureID());
    }

    public void setFirstPicture(String firstPicture){
        aboutList.get(0).setPictureID(pictureDao.getIDForName(firstPicture));
    }

    public void setSecondPicture(String secondPicture){
        aboutList.get(1).setPictureID(pictureDao.getIDForName(secondPicture));
    }

    public void setThirdPicture(String thirdPicture){
        aboutList.get(2).setPictureID(pictureDao.getIDForName(thirdPicture));
    }
}
