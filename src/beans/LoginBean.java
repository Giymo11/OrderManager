package beans;

/**
 *
 * @author Markus
 */

import dao.LoginDao;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@ManagedBean
@SessionScoped
public class LoginBean {
    private LoginDao loginDao;
    public static String status;
    private String currentMail;

    public String getStatus() {
        return status;
    }

    public LoginBean() {
        loginDao = new LoginDao();
    }

    @PostConstruct
    private void postConstruct(){
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Boolean loggedin = (Boolean) req.getSession().getAttribute("loggedIn");
        if(req.getSession() == null)
            System.out.println("Session is null");
        else{
        if(loggedin == null || !loggedin)
            status = "Anmelden";
        else {
            status = "Abmelden";
            currentMail = (String) req.getSession().getAttribute("email");
        }}

    }

    public String getWrongPassword() {
        return loginDao.getWrongPassword();
    }

    public void setWrongPassword(String wrongPassword) {
        loginDao.setWrongPassword(wrongPassword);
    }

    public String getEmail() {
        return loginDao.getEmail();
    }

    public void setEmail(String email) {
        loginDao.setEmail(email);
    }

    public String getPassword() {
        return loginDao.getPassword();
    }

    public void setPassword(String password) {
        loginDao.setPassword(password);
    }

    public String check() throws IOException {
        HttpServletRequest req = null;

        if (FacesContext.getCurrentInstance() != null) {
            req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        }
        String returnValue = loginDao.check(req);
        if(returnValue.equals("notVerified"))
            FacesContext.getCurrentInstance().addMessage("Fail", new FacesMessage("Diese Addresse wurde noch nicht vom Administrator bestätigt. Bitte versuchen Sie es später erneut"));

        if ( returnValue.equals("blocked") )
            FacesContext.getCurrentInstance().addMessage("Fail", new FacesMessage("Diese Adresse wurde blockiert!"));

        if(returnValue.equals("")) {
            status = "Anmelden";
            currentMail = "";
        }
        else {
            status = "Abmelden";
            if(req.getSession()!=null)
                currentMail = req.getSession().getAttribute("email") + "";
        }

        return returnValue;
    }

    public String handleClick(){
        HttpServletRequest req = null;

        if (FacesContext.getCurrentInstance() != null) {
            req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        }
        if(status.equals("Anmelden")){
            req.setAttribute("loggedIn", false);
            req.setAttribute("email", null);
            req.setAttribute("adminLoggedIn", false);
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            return "/login.xhtml?faces-redirect=true";
        }
        else{
            req.setAttribute("loggedIn", false);
            req.setAttribute("email", null);
            req.setAttribute("adminLoggedIn", false);
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            status="Anmelden";
            return "/offers.xhtml?faces-redirect=true";
        }
    }

    @PreDestroy
    public void preDestroy(){
        HttpServletRequest req = null;

        if (FacesContext.getCurrentInstance() != null) {
            req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        }
        req.setAttribute("loggedIn", false);
        req.setAttribute("email", null);
        req.setAttribute("adminLoggedIn", false);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    public String getCurrentMail(){
        return currentMail;
    }

    public String logout() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        req.setAttribute("loggedIn", false);
        req.setAttribute("email", null);
        req.setAttribute("adminLoggedIn", false);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        status="Anmelden";
        return "/offers.xhtml?faces-redirect=true";
    }
}
