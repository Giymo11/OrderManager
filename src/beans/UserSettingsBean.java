package beans;

import dao.AddressDao;
import dao.UserSettingsDao;
import dto.Address;
import dto.User;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Sarah on 30.03.2014.
 */
@ManagedBean
@RequestScoped
public class UserSettingsBean {
    private String newEmail;
    private String oldPass;
    private String newPass1;
    private String newPass2;

    private User user;
    private Address address;

    private UserSettingsDao settingsDAO;
    private String selectedTown;

    public UserSettingsBean(){
        settingsDAO = new UserSettingsDao();

        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        user = settingsDAO.getUser(req.getSession().getAttribute("email").toString());
        address = new AddressDao().getAddressWithID(user.getAddressID());
        newEmail = user.getEmail();
        oldPass = null;
        newPass1 = "";
        newPass2 = "";

        selectedTown = settingsDAO.getSelectedTown(user);
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass1() {
        return newPass1;
    }

    public void setNewPass1(String newPass1) {
        this.newPass1 = newPass1;
    }

    public String getNewPass2() {
        return newPass2;
    }

    public void setNewPass2(String newPass2) {
        this.newPass2 = newPass2;
    }

    public void saveLoginData(){
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String email = req.getSession().getAttribute("email").toString();

        if(settingsDAO.check(oldPass, email)){
            if(newPass1.equals(newPass2)){
                int check = settingsDAO.writeEmailAndPass(email, newEmail, newPass1, oldPass);
                if (check == 1) {
                    if(!newEmail.equals(""))
                        req.getSession().setAttribute("email", newEmail);
                    FacesContext.getCurrentInstance().addMessage("Success", new FacesMessage("Änderungen erfolgreich übernommen"));
                }
            }
            else{
                FacesContext.getCurrentInstance().addMessage("Failure", new FacesMessage("Die Passwörter stimmen nicht überein!"));
            }
        }
        else{
            FacesContext.getCurrentInstance().addMessage("Failure", new FacesMessage("Passwort nicht korrekt"));
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void saveUserData(){
        settingsDAO.saveUserData(user, address, selectedTown);
    }

    public void setSelectedTown(String selectedTown) {
        this.selectedTown = selectedTown;
    }

    public String getSelectedTown() {
        return selectedTown;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
