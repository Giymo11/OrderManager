package beans;

import dbaccess.ConnectionManager;
import dto.Adress;
import dto.User;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: Markus
 * Date: 05.02.14
 * Time: 13:20
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
public class RegisterBean {
    private String email, password, passwordWdh, salt, firstName, lastName, hash, location, street, houseNr, telNr;
    private int id, adressId, lastID, plz;
    private ConnectionManager connectionManager;
    private Connection connection;
    private Adress adress;

    public RegisterBean() {
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("jdbc/dataSource", false);
        lastID = 0;
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

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
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


    public void register() {
        if (password.equals(passwordWdh)) {
            String hash = this.hash(password);
            this.id = getIDUser();
            this.adressId = getIDAdress();
            this.adress = new Adress(adressId, plz, location, street, houseNr);
            User u = new User(email, email, firstName, lastName, hash, id, telNr, adressId);
            this.adress = new Adress(this.getIDAdress(), plz, location, street, houseNr);
            try {
                connection.createStatement().executeUpdate(" INSERT INTO ordermanager.adress VALUES(" + adress.getSQLString() + ");");
                connection.createStatement().executeUpdate("INSERT INTO ordermanager.user VALUES(" + u.getSQLString() + ");");
                connection.createStatement().executeUpdate("COMMIT;");
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


        } else {
            FacesContext.getCurrentInstance().addMessage("FAILURE", new FacesMessage("Passwörter stimmen nicht überein"));
        }
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

    public int getIDUser() {
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT id from ordermanager.user");

            this.lastID = 0;
            while (resultSet.next()) {
                if (this.lastID <= resultSet.getInt(1))
                    this.lastID = resultSet.getInt(1);
            }

            resultSet.close();
            return this.lastID + 1;
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return 0;
    }

    public int getIDAdress() {
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT id from ordermanager.adress");

            this.lastID = 0;
            while (resultSet.next()) {
                if (this.lastID <= resultSet.getInt(1))
                    this.lastID = resultSet.getInt(1);
            }

            resultSet.close();

            return this.lastID + 1;

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return 0;
    }


}

