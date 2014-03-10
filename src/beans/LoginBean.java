package beans;

/**
 *
 * @author Markus
 */

import dao.LoginDAO;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@ManagedBean
@SessionScoped
public class LoginBean {
    private LoginDAO loginDAO;
    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LoginBean() {
        loginDAO = new LoginDAO();
        status = "Anmelden";

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

    public String handleClick(){
        HttpServletRequest req = null;

        if (FacesContext.getCurrentInstance() != null) {
            req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        }
        if(status.equals("Anmelden")){
            status="Abmelden";
            return "/login.xhtml?faces-redirect=true";
        }
        else{
                req.setAttribute("loggedIn", false);
                req.setAttribute("email", null);
                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                status="Anmelden";
                return "/offers.xhtml?faces-redirect=true";
        }
    }

}
