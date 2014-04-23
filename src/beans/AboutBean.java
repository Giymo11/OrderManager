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
    private AboutDAO aboutDAO;

    public AboutBean() {
        aboutDAO = new AboutDAO();
    }

    public About getFirst() {
        return aboutDAO.getAboutList().get(0);
    }

    public About getSecond() {
        return aboutDAO.getAboutList().get(1);
    }

    public About getThird() {
        return aboutDAO.getAboutList().get(2);
    }

    public List<About> getAboutList(){
        return aboutDAO.getAboutList();
    }

    public void save(){
        aboutDAO.save();
    }
}
