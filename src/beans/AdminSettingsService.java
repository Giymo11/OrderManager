package beans;

import dao.AdminSettingsDao;
import dao.UserAdminDao;
import dto.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Map;

/**
 * Created by Sarah on 03.04.2014.
 */
@ManagedBean
@SessionScoped
public class AdminSettingsService {
    private UserAdminDao userAdminDao;
    private AdminSettingsDao adminSettingsDao;
    private List<User> userList;

    public AdminSettingsService(){
        userAdminDao = new UserAdminDao();
        adminSettingsDao = new AdminSettingsDao();
    }

    public List<User> getUserList(){
        if(userList == null)
            userList = userAdminDao.getUserList();

        return userList;
    }

    public void save(){
        int id = Integer.parseInt(fetchParameter("idU"));
        userAdminDao.save(id, userList);
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }

    public void addAsAdmin(){
        int id = Integer.parseInt(fetchParameter("idAdmin"));
        adminSettingsDao.addAsAdmin(id);
        userList = userAdminDao.getUserList();
    }
}
