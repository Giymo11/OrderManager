package beans;

import dao.AdminSettingsDao;
import dao.UserAdminDAO;
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
    private UserAdminDAO userAdminDAO;
    private AdminSettingsDao adminSettingsDao;
    private String pass;
    private String email;
    private List<User> userList;

    public AdminSettingsService(){
        userAdminDAO = new UserAdminDAO();
        adminSettingsDao = new AdminSettingsDao();
    }

    public List<User> getUserList(){
        if(userList == null)
            userList = userAdminDAO.getUserList();

        return userList;
    }

    public void save(){
        int id = Integer.parseInt(fetchParameter("idU"));
        userAdminDAO.save(id, userList);
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }

    public void addAsAdmin(){
        int id = Integer.parseInt("idAdmin");
        adminSettingsDao.addAsAdmin(email, pass, id);
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public String getEmail() {
        return email;
    }
}
