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
public class LoginDAO extends JdbcDao {
    private String email, wrongPassword;
    private String password;

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
            res = stat.executeQuery("SELECT Hash, verified, blocked from " + DATABASE_NAME + ".user where Email = '" + email + "';");
            if(res.next()){
                String checkHash = res.getString(1);
                boolean verified = res.getBoolean(2);
                boolean blocked = res.getBoolean(3);
                if(!verified)
                    return "notVerified";

                if(blocked)
                    return "blocked";

                if (checkHash.equals(hash(password.concat(email)))) {
                    wrongPassword = "";

                    req.getSession().setAttribute("email", this.email);
                    if(email.equals("baeckerei.pock@a1.net")){
                        req.getSession().setAttribute("adminLoggedIn", true);
                    }
                    else{
                        req.getSession().setAttribute("loggedIn", true);
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
