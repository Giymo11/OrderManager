package beans;

import dao.AboutDao;
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
    private List<About> aboutList;

    public AboutService() {
        aboutDao = new AboutDao();
    }

    public About getFirst() {
        if (aboutList == null) {
            aboutList = aboutDao.getAboutList();
        }
        if(!aboutList.isEmpty())
            return aboutList.get(0);
        return new About(0, "", 0);
    }

    public About getSecond() {
        if (aboutList == null) {
            aboutList = aboutDao.getAboutList();
        }
        if(aboutList.size()>1)
            return aboutList.get(1);
        return new About(0, "", 0);
    }

    public About getThird() {
        if (aboutList == null) {
            aboutList = aboutDao.getAboutList();
        }
        if(aboutList.size()>2)
            return aboutList.get(2);
        return new About(0, "", 0);
    }

    public void save(){
        aboutDao.save(aboutList);
    }
}
