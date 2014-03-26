package dao;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 25.02.14
 * Time: 20:27
 * To change this template use File | Settings | File Templates.
 */
public class LoginDAO extends JDBCDAO {
    private String email, checkEmail, wrongPassword;
    private String password;
    private String checkHash, checkSalt;

    public LoginDAO(){
        super();
    }

    public String check(HttpServletRequest req) {
        Connection connection = null;
        Statement stat = null;
        ResultSet res = null;
        try {
            connection = getConnection();
            stat = connection.createStatement();
            res = stat.executeQuery("SELECT Email, Hash, Salt from ordermanager.user where Email = '" + email + "';");
            if(res.next()){
                checkEmail = res.getString(1);
                checkHash = res.getString(2);
                checkSalt = res.getString(3);

                if (email.equals(checkEmail) && checkHash.equals(hash(password+email)) && email.equals(checkSalt)) {
                    wrongPassword = "";

                    req.getSession().setAttribute("email", this.email);
                    if(email.equals("baeckerei.pock@a1.net")){
                        req.getSession().setAttribute("adminLoggedIn", true);
                        System.out.println("Admin eingeloggt");
                    }
                    else{
                        req.getSession().setAttribute("loggedIn", true);
                        System.out.println("User eingeloggt");
                    }

                    return "/offers.xhtml?faces-redirect=true";
                } else {
                    wrongPassword = "Falsches Passwort!";
                }
            }
            else{
               wrongPassword = "Falsche Email Adresse";
            }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(res, stat, connection);
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

        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
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
}
