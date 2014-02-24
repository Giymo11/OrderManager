package beans;

/**
 *
 * @author Markus
 */

import dbaccess.ConnectionManager;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@ManagedBean
public class LoginBean {
    private String email, checkEmail, wrongPassword;
    private String password;
    private String checkHash, checkSalt;
    private ConnectionManager connectionManager;
    private Connection connection;

    public LoginBean() {
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("jdbc/dataSource", false);
    }

    public String getWrongPassword() {
        return wrongPassword;
    }

    public void setWrongPassword(String wrongPassword) {
        this.wrongPassword = wrongPassword;
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

    public String check() throws IOException {

        HttpServletRequest req = null;

        if (FacesContext.getCurrentInstance() != null) {
            req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        }


        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT Email, Hash, Salt from ordermanager.user where Email = '" + this.email + "';");
            resultSet.next();
            this.checkEmail = resultSet.getString(1);
            this.checkHash = resultSet.getString(2);
            this.checkSalt = resultSet.getString(3);

            resultSet.close();
            statement.close();
            connection.close();

            if (email.equals(checkEmail) && checkHash.equals(this.hash(password)) && email.equals(checkSalt)) {
                System.out.println("Vergleich mit DB erfolgreich!");
                this.wrongPassword = "";
                req.getSession().setAttribute("loggedIn", true);
                return "/offers.xhtml?faces-redirect=true";
            } else {
                System.out.println("Test");
                this.wrongPassword = "Falsches Passwort!";

            }


        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        return "";
    }


    public String hash(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        System.out.println("Hex format : " + sb.toString());

        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


}
