package beans;

import dao.RegisterDAO;
import dto.Address;
import dto.User;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Markus
 * Date: 05.02.14
 * Time: 13:20
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
public class RegisterBean {
    private String email, password, passwordWdh, firstName, lastName, location, street, houseNr, telNr;
    private String selectedTown;
    private Date birthdate;
    private Address address;
    private RegisterDAO registerDAO;


    public RegisterBean() {
        registerDAO = new RegisterDAO();
        getGetMaxDate();
    }

    public void register() {
        if (password.equals(passwordWdh)) {
            String hash = hash(password.concat(email));


            address = new Address(street, houseNr);
            User u = new User(email, firstName, lastName, hash, telNr, birthdate, -1, false, false);

            registerDAO.register(u, address, selectedTown);

        } else {
            FacesContext.getCurrentInstance().addMessage("FAILURE", new FacesMessage("Passwörter stimmen nicht überein"));
        }
    }

    public String hash(String saltedPass) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(saltedPass.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuilder sb = new StringBuilder();
        for (byte aByteData1 : byteData) {
            sb.append(Integer.toString((aByteData1 & 0xff) + 0x100, 16).substring(1));
        }

        //convert the byte to hex format method 2
        StringBuilder hexString = new StringBuilder();
        for (byte aByteData : byteData) {
            String hex = Integer.toHexString(0xff & aByteData);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNr() {
        return houseNr;
    }

    public void setHouseNr(String houseNr) {
        this.houseNr = houseNr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordWdh() {
        return passwordWdh;
    }

    public void setPasswordWdh(String passwordWdh) {
        this.passwordWdh = passwordWdh;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelNr() {
        return telNr;
    }

    public void setTelNr(String telNr) {
        this.telNr = telNr;
    }

    public String getSelectedTown() {
        return selectedTown;
    }

    public void setSelectedTown(String selectedTown) {
        this.selectedTown = selectedTown;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Date getBirthdate(){
        return birthdate;
    }

    public Date getGetMaxDate() {
        Date date = new Date();
        date.setYear(date.getYear()+1882);
        birthdate = date;
        return date;
    }
}

