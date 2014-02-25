package beans;

/**
 *
 * @author Markus
 */

import dao.LoginDAO;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@ManagedBean
public class LoginBean {
    private LoginDAO loginDAO;

    public LoginBean() {
        loginDAO = new LoginDAO();
    }

    public String getWrongPassword() {
        return loginDAO.getWrongPassword();
    }

    public void setWrongPassword(String wrongPassword) {
        loginDAO.setWrongPassword(wrongPassword);
    }

    public String getEmail() {
        return loginDAO.getEmail();
    }

    public void setEmail(String email) {
        loginDAO.setEmail(email);
    }

    public String getPassword() {
        return loginDAO.getPassword();
    }

    public void setPassword(String password) {
        loginDAO.setPassword(password);
    }

    public String check() throws IOException {
        HttpServletRequest req = null;

        if (FacesContext.getCurrentInstance() != null) {
            req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        }
        return loginDAO.check(req);
    }
}
