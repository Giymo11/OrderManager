package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 25.02.14
 * Time: 21:33
 * To change this template use File | Settings | File Templates.
 */
public class ContactInfoDao extends JdbcDao {
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

    public ContactInfoDao(){
        super();
        read();
    }

    private void read(){
        Connection connection = null;
        Statement statement = null;
        Statement statementInfo = null;
        ResultSet resHours = null;
        ResultSet resInfo = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statementInfo = connection.createStatement();

            resHours = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".openinghours");
            resInfo = statementInfo.executeQuery("SELECT * FROM " + DATABASE_NAME + ".contactinfo");

            if (!resHours.isClosed())
                while (resHours.next()) {
                    setDays(resHours);
                    if (resHours.isLast())
                        break;
                }
            if(resInfo.next())
                setInfo(resInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resHours, statement, connection);
            close(resInfo, statementInfo, null);
        }
    }

    private void setInfo(ResultSet res) throws SQLException {
        name = res.getString(1);
        street = res.getString(2);
        location = res.getString(3);
        telephone = res.getString(4);
        mail = res.getString(5);
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

    public void writeToDB() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();

            statement.executeUpdate("UPDATE " + DATABASE_NAME + ".contactinfo SET name = '" + name + "', street = '" + street +
                    "', location = '" + location + "', telephone = '" + telephone + "', email = '" + mail + "';");
            statement.executeUpdate("DELETE FROM " + DATABASE_NAME + ".openinghours;");

            if (mondayAM.length() > 1)
                statement.executeUpdate("INSERT INTO " + DATABASE_NAME + ".openinghours " +
                        "VALUES(1, '" + mondayAM.substring(0, 5) + "', '" + mondayAM.replace("-", "").substring(5) + "');");

            if (mondayPM.length() > 1)
                statement.executeUpdate("INSERT INTO " + DATABASE_NAME + ".openinghours " +
                        "VALUES(1, '" + mondayPM.substring(0, 5) + "', '" + mondayPM.replace("-", "").substring(5) + "');");

            if (thursdayAM.length() > 1)
                statement.executeUpdate("INSERT INTO " + DATABASE_NAME + ".openinghours " +
                        "VALUES(4, '" + thursdayAM.substring(0, 5) + "', '" + thursdayAM.replace("-", "").substring(5) + "');");

            if (thursdayPM.length() > 1)
                statement.executeUpdate("INSERT INTO " + DATABASE_NAME + ".openinghours " +
                        "VALUES(4, '" + thursdayPM.substring(0, 5) + "', '" + thursdayPM.replace("-", "").substring(5) + "');");

            if (wednesdayAM.length() > 1)
                statement.executeUpdate("INSERT INTO " + DATABASE_NAME + ".openinghours " +
                        "VALUES(3, '" + wednesdayAM.substring(0, 5) + "', '" + wednesdayAM.replace("-", "").substring(5) + "');");

            if (wednesdayPM.length() > 1)
                statement.executeUpdate("INSERT INTO " + DATABASE_NAME + ".openinghours " +
                        "VALUES(3, '" + wednesdayPM.substring(0, 5) + "', '" + wednesdayPM.replace("-", "").substring(5) + "');");

            if (tuesdayAM.length() > 1)
                statement.executeUpdate("INSERT INTO " + DATABASE_NAME + ".openinghours " +
                        "VALUES(2, '" + tuesdayAM.substring(0, 5) + "', '" + tuesdayAM.replace("-", "").substring(5) + "');");

            if (tuesdayPM.length() > 1)
                statement.executeUpdate("INSERT INTO " + DATABASE_NAME + ".openinghours " +
                        "VALUES(2, '" + tuesdayPM.substring(0, 5) + "', '" + tuesdayPM.replace("-", "").substring(5) + "');");

            if (fridayAM.length() > 1)
                statement.executeUpdate("INSERT INTO " + DATABASE_NAME + ".openinghours " +
                        "VALUES(5, '" + fridayAM.substring(0, 5) + "', '" + fridayAM.replace("-", "").substring(5) + "');");

            if (fridayPM.length() > 1)
                statement.executeUpdate("INSERT INTO " + DATABASE_NAME + ".openinghours " +
                        "VALUES(5, '" + fridayPM.substring(0, 5) + "', '" + fridayPM.replace("-", "").substring(5) + "');");

            if (saturdayAM.length() > 1)
                statement.executeUpdate("INSERT INTO " + DATABASE_NAME + ".openinghours " +
                        "VALUES(6, '" + saturdayAM.substring(0, 5) + "', '" + saturdayAM.replace("-", "").substring(5) + "');");

            if (saturdayPM.length() > 1)
                statement.executeUpdate("INSERT INTO " + DATABASE_NAME + ".openinghours " +
                        "VALUES(6, '" + saturdayPM.substring(0, 5) + "', '" + saturdayPM.replace("-", "").substring(5) + "');");

            connection.createStatement().executeUpdate("COMMIT;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(null, statement, connection);
        }
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
        if(!mondayAM.isEmpty() && !mondayAM.substring(0,1).equals("0") && !mondayAM.substring(0,1).equals("1"))
            this.mondayAM = "0" + this.mondayAM;
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
        if(!tuesdayAM.isEmpty() && !tuesdayAM.substring(0,1).equals("0") && !tuesdayAM.substring(0,1).equals("1"))
            this.tuesdayAM = "0" + this.tuesdayAM;
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
        if(!wednesdayAM.isEmpty() && !wednesdayAM.substring(0,1).equals("0") && !wednesdayAM.substring(0,1).equals("1"))
            this.wednesdayAM = "0" + this.wednesdayAM;
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
        if(!thursdayAM.isEmpty() && !thursdayAM.substring(0,1).equals("0") && !thursdayAM.substring(0,1).equals("1"))
            this.thursdayAM = "0" + this.thursdayAM;
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
        if(!fridayAM.isEmpty() && !fridayAM.substring(0,1).equals("0") && !fridayAM.substring(0,1).equals("1"))
            this.fridayAM = "0" + this.fridayAM;
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
        if(!saturdayAM.isEmpty() && !saturdayAM.substring(0,1).equals("0") && !saturdayAM.substring(0,1).equals("1"))
            this.saturdayAM = "0" + this.saturdayAM;
    }

    public String getSaturdayPM() {
        return saturdayPM;
    }

    public void setSaturdayPM(String saturdayPM) {
        this.saturdayPM = saturdayPM.replace(" ", "");
    }
}
