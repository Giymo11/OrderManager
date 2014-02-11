package beans;

import dbaccess.ConnectionManager;

import javax.faces.bean.ManagedBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 22.01.14
 * Time: 13:33
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
public class PictureBean {
    List<String> pictureList;
    private int lastID = 0;
    private ConnectionManager connectionManager;
    private java.sql.Connection connection;

    public PictureBean() {
        pictureList = new ArrayList();
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("jdbc/dataSource", false);
        read();
    }

    public List<String> getPictureList() {
        if (pictureList.isEmpty()) {
            read();
        }
        return pictureList;
    }

    private void read() {
        try {
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM ordermanager.picture");
            while (res.next()) {
                if (lastID <= res.getInt(1))
                    lastID = res.getInt(1);
                pictureList.add(res.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPictureList(List<String> list) {
        pictureList = list;
    }

    public int getIDWithString(String name) {
        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT pictureid FROM ordermanager.picture WHERE name = '" + name + "';");
            res.next();
            int id = res.getInt(1);
            res.close();
            return id;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public String getStringWithID(int id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery("SELECT name FROM ordermanager.picture WHERE pictureid = " + id + ";");

            res.next();
            return res.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }

    public void addNewPicture(String name) {
        try {
            connection.createStatement().executeUpdate("INSERT INTO ordermanager.picture VALUES(" + ++lastID + ", '" + name + "');");
            connection.createStatement().executeUpdate("Commit;");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}