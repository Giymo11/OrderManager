/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dbaccess.ConnectionManager;

import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Sarah
 */

@ManagedBean
@SessionScoped
public class ContactInfoBean {
    private String name;
    private String street;
    private String location;
    private String telephone;
    private String mail;

    private String mondayAM;
    private String mondayPM;
    private String tuesdayAM;
    private String tuesdayPM;
    private String wednesdayAM;
    private String wednesdayPM;
    private String thursdayAM;
    private String thursdayPM;
    private String fridayAM;
    private String fridayPM;
    private String saturdayAM;
    private String saturdayPM;

    private ConnectionManager connectionManager;
    private Connection connection;

    public ContactInfoBean() {
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("jdbc/dataSource", false);
        read();
    }

    private void read() {
        try {
            Statement statement = connection.createStatement();
            Statement statementInfo = connection.createStatement();

            ResultSet resHours = statement.executeQuery("SELECT * FROM ordermanager.openinghours");
            ResultSet resInfo = statementInfo.executeQuery("SELECT * FROM ordermanager.contactinfo");

            if (!resHours.isClosed()) {
                while (resHours.next()) {
                    setDays(resHours);
                    if (resHours.isLast())
                        break;
                }
            } else
                System.out.println("resHours is closed");

            if (resInfo.first() == true)
                setInfo(resInfo);

            resHours.close();
            resInfo.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void setDays(ResultSet res) throws SQLException {
        String str = res.getString(2);      //returns start of working hours. getString(3) returns end of working hours
        int firstOpenHour = 0;

        //init firstOpenHour with the first two digits of the first working hour
        if (str != null) {
            str = str.substring(0, 2);
            firstOpenHour = Integer.valueOf(str);
        }

        //res.getInt(1) returns day of the week
        switch (res.getInt(1)) {
            case 1:
                if (firstOpenHour < 12)
                    mondayAM = res.getString(2) + " - " + res.getString(3);
                else
                    mondayPM = res.getString(2) + " - " + res.getString(3);
                break;
            case 2:
                if (firstOpenHour < 12)
                    tuesdayAM = res.getString(2) + " - " + res.getString(3);
                else
                    tuesdayPM = res.getString(2) + " - " + res.getString(3);
                break;
            case 3:
                if (firstOpenHour < 12)
                    wednesdayAM = res.getString(2) + " - " + res.getString(3);
                else
                    wednesdayPM = res.getString(2) + " - " + res.getString(3);
                break;
            case 4:
                if (firstOpenHour < 12)
                    thursdayAM = res.getString(2) + " - " + res.getString(3);
                else
                    thursdayPM = res.getString(2) + " - " + res.getString(3);
                break;
            case 5:
                if (firstOpenHour < 12)
                    fridayAM = res.getString(2) + " - " + res.getString(3);
                else
                    fridayPM = res.getString(2) + " - " + res.getString(3);
                break;
            case 6:
                if (firstOpenHour < 12)
                    saturdayAM = res.getString(2) + " - " + res.getString(3);
                else
                    saturdayPM = res.getString(2) + " - " + res.getString(3);
                break;
        }
    }

    private void setInfo(ResultSet res) throws SQLException {
        name = res.getString(1);
        street = res.getString(2);
        location = res.getString(3);
        telephone = res.getString(4);
        mail = res.getString(5);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTelephone() {
        return telephone;

    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMondayAM() {
        return mondayAM;
    }

    public void setMondayAM(String mondayAM) {
        this.mondayAM = mondayAM.replace(" ", "");
    }

    public String getMondayPM() {
        return mondayPM;
    }

    public void setMondayPM(String mondayPM) {
        this.mondayPM = mondayPM.replace(" ", "");
    }

    public String getTuesdayAM() {
        return tuesdayAM;
    }

    public void setTuesdayAM(String tuesdayAM) {
        this.tuesdayAM = tuesdayAM.replace(" ", "");
    }

    public String getTuesdayPM() {
        return tuesdayPM;
    }

    public void setTuesdayPM(String tuesdayPM) {
        this.tuesdayPM = tuesdayPM.replace(" ", "");
    }

    public String getWednesdayAM() {
        return wednesdayAM;
    }

    public void setWednesdayAM(String wednesdayAM) {
        this.wednesdayAM = wednesdayAM.replace(" ", "");
    }

    public String getWednesdayPM() {
        return wednesdayPM;
    }

    public void setWednesdayPM(String wednesdayPM) {
        this.wednesdayPM = wednesdayPM.replace(" ", "");
    }

    public String getThursdayAM() {
        return thursdayAM;
    }

    public void setThursdayAM(String thursdayAM) {
        this.thursdayAM = thursdayAM.replace(" ", "");
    }

    public String getThursdayPM() {
        return thursdayPM;
    }

    public void setThursdayPM(String thursdayPM) {
        this.thursdayPM = thursdayPM.replace(" ", "");
    }

    public String getFridayAM() {
        return fridayAM;
    }

    public void setFridayAM(String fridayAM) {
        this.fridayAM = fridayAM.replace(" ", "");
    }

    public String getFridayPM() {
        return fridayPM;
    }

    public void setFridayPM(String fridayPM) {
        this.fridayPM = fridayPM.replace(" ", "");
    }

    public String getSaturdayAM() {
        return saturdayAM;
    }

    public void setSaturdayAM(String saturdayAM) {
        this.saturdayAM = saturdayAM.replace(" ", "");
    }

    public String getSaturdayPM() {
        return saturdayPM;
    }

    public void setSaturdayPM(String saturdayPM) {
        this.saturdayPM = saturdayPM.replace(" ", "");
    }

    public void writeToDB() {
        try {

            System.out.println("writeToDB");
            connection.createStatement().executeUpdate("DELETE FROM ordermanager.contactinfo");
            connection.createStatement().executeUpdate("INSERT INTO ordermanager.contactinfo VALUES('" + name + "', '" + street +
                    "', '" + location + "', '" + telephone + "', '" + mail + "');");

            connection.createStatement().executeUpdate("DELETE FROM ordermanager.openinghours;");

            if (mondayAM.length() > 1)
                connection.createStatement().executeUpdate("INSERT INTO ordermanager.openinghours " +
                        "VALUES(1, '" + mondayAM.substring(0, 5) + "', '" + mondayAM.replace("-", "").substring(5) + "');");

            if (mondayPM.length() > 1)
                connection.createStatement().executeUpdate("INSERT INTO ordermanager.openinghours " +
                        "VALUES(1, '" + mondayPM.substring(0, 5) + "', '" + mondayPM.replace("-", "").substring(5) + "');");

            if (thursdayAM.length() > 1)
                connection.createStatement().executeUpdate("INSERT INTO ordermanager.openinghours " +
                        "VALUES(4, '" + thursdayAM.substring(0, 5) + "', '" + thursdayAM.replace("-", "").substring(5) + "');");

            if (thursdayPM.length() > 1)
                connection.createStatement().executeUpdate("INSERT INTO ordermanager.openinghours " +
                        "VALUES(4, '" + thursdayPM.substring(0, 5) + "', '" + thursdayPM.replace("-", "").substring(5) + "');");

            if (wednesdayAM.length() > 1)
                connection.createStatement().executeUpdate("INSERT INTO ordermanager.openinghours " +
                        "VALUES(3, '" + wednesdayAM.substring(0, 5) + "', '" + wednesdayAM.replace("-", "").substring(5) + "');");

            if (wednesdayPM.length() > 1)
                connection.createStatement().executeUpdate("INSERT INTO ordermanager.openinghours " +
                        "VALUES(3, '" + wednesdayPM.substring(0, 5) + "', '" + wednesdayPM.replace("-", "").substring(5) + "');");

            if (tuesdayAM.length() > 1)
                connection.createStatement().executeUpdate("INSERT INTO ordermanager.openinghours " +
                        "VALUES(2, '" + tuesdayAM.substring(0, 5) + "', '" + tuesdayAM.replace("-", "").substring(5) + "');");

            if (tuesdayPM.length() > 1)
                connection.createStatement().executeUpdate("INSERT INTO ordermanager.openinghours " +
                        "VALUES(2, '" + tuesdayPM.substring(0, 5) + "', '" + tuesdayPM.replace("-", "").substring(5) + "');");

            if (fridayAM.length() > 1)
                connection.createStatement().executeUpdate("INSERT INTO ordermanager.openinghours " +
                        "VALUES(5, '" + fridayAM.substring(0, 5) + "', '" + fridayAM.replace("-", "").substring(5) + "');");

            if (fridayPM.length() > 1)
                connection.createStatement().executeUpdate("INSERT INTO ordermanager.openinghours " +
                        "VALUES(5, '" + fridayPM.substring(0, 5) + "', '" + fridayPM.replace("-", "").substring(5) + "');");

            if (saturdayAM.length() > 1)
                connection.createStatement().executeUpdate("INSERT INTO ordermanager.openinghours " +
                        "VALUES(6, '" + saturdayAM.substring(0, 5) + "', '" + saturdayAM.replace("-", "").substring(5) + "');");

            if (saturdayPM.length() > 1)
                connection.createStatement().executeUpdate("INSERT INTO ordermanager.openinghours " +
                        "VALUES(6, '" + saturdayPM.substring(0, 5) + "', '" + saturdayPM.replace("-", "").substring(5) + "');");

            connection.createStatement().executeUpdate("COMMIT;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("ContactinfoBean PreDestroy");

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
