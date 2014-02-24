package beans;

import dao.AboutDAO;
import dto.About;

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
public class AboutBean {
    private List<About> aboutList;
    private AboutDAO aboutDAO;

    public AboutBean() {
        aboutDAO = new AboutDAO();
        aboutList = aboutDAO.getAboutList();
    }

    public About getFirst() {
        return aboutList.get(0);
    }

    public About getSecond() {
        return aboutList.get(1);
    }

    public About getThird() {
        return aboutList.get(2);
    }
}
