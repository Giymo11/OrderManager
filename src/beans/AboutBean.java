package beans;

import dbaccess.ConnectionManager;
import dto.About;

import javax.faces.bean.ManagedBean;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 17.02.14
 * Time: 14:16
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
public class AboutBean {
    private List<About> aboutList;
    private Connection connection;

    private About first;
    private About second;
    private About third;

    public AboutBean() {
        ConnectionManager cm = new ConnectionManager();
        connection = cm.getConnection("jdbc/dataSource", false);
        aboutList = new ArrayList<>();
        read();
    }

    private void read() {
        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT * FROM ordermanager.about;");

            while (res.next()) {
                aboutList.add(getAboutWithResultSet(res));
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private About getAboutWithResultSet(ResultSet res) throws SQLException {
        return new About(res.getInt(1), res.getString(2), res.getInt(3));
    }

    public About getFirst() {
        System.out.println("getFirstCalled");
        first = aboutList.get(0);
        return first;
    }

    public About getSecond() {
        second = aboutList.get(1);
        return second;
    }

    public About getThird() {
        third = aboutList.get(2);
        return third;
    }
}
