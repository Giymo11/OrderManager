package dao;

import dto.About;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 24.02.14
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
public class AboutDAO extends JDBCDAO {
    private List<About> aboutList;

    public AboutDAO() {
        aboutList = new ArrayList<>();
    }

    private void read(){
        ResultSet res = null;
        ResultSet res2 = null;
        Statement stat = null;
        Statement stat2 = null;
        Connection connection = null;
        try {
            connection = getConnection();
            stat = connection.createStatement();
            res = stat.executeQuery("SELECT * FROM " + DATABASE_NAME + ".about;");
            stat2 = connection.createStatement();
            About about;

            while (res.next()) {
                res2 = stat2.executeQuery("SELECT name FROM " + DATABASE_NAME + ".picture WHERE pictureid = " + res.getInt(3) + ";");
                res2.next();

                about = getAboutWithResultSet(res);
                about.setPictureName(res2.getString(1));
                aboutList.add(about);

            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(res, stat, connection);
            close(res2, stat2, null);
        }
    }

    private About getAboutWithResultSet(ResultSet res) throws SQLException {
        return new About(res.getInt(1), res.getString(2), res.getInt(3));
    }

    public List<About> getAboutList(){
        if(aboutList.isEmpty())
            read();
        return aboutList;
    }
}
