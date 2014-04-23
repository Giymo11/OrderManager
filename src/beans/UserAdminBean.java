package beans;

import dao.UserAdminDAO;
import dto.User;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Map;

/**
 * Created by Sarah on 03.04.2014.
 */
@ManagedBean
public class UserAdminBean {
    private UserAdminDAO userAdminDAO;

    public UserAdminBean(){
        userAdminDAO = new UserAdminDAO();
    }

    public List<User> getUserList(){
        return userAdminDAO.getUserList();
    }

    public void save(){
        int id = Integer.parseInt(fetchParameter("idU"));
        userAdminDAO.save(id);
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }
}
