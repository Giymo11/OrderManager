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
public class AboutDao extends JdbcDao {
    public AboutDao() {
        super();
    }

    public List<About> getAboutList(){
        About about;
        List<About> aboutList = new ArrayList();
        ResultSet res = null;
        ResultSet resCount = null;
        Statement stat = null;
        Statement stat3 = null;
        Statement stat4 = null;
        Connection connection = null;

        try {
            connection = getConnection();
            stat = connection.createStatement();
            stat3 = connection.createStatement();
            stat4 = connection.createStatement();

            resCount = stat3.executeQuery("SELECT count(*) FROM " + DATABASE_NAME + ".about;");
            resCount.next();

            if(resCount.getInt(1)==0) {
                stat4.executeUpdate("INSERT INTO " + DATABASE_NAME + ".about VALUES(1, 'a', 1);");
                stat4.executeUpdate("INSERT INTO " + DATABASE_NAME + ".about VALUES(2, 'a', 1);");
                stat4.executeUpdate("INSERT INTO " + DATABASE_NAME + ".about VALUES(3, 'a', 1);");
                stat4.executeUpdate("COMMIT;");
            }

            res = stat.executeQuery("SELECT * FROM " + DATABASE_NAME + ".about;");
            while (res.next()) {
                about = getAboutWithResultSet(res);

                aboutList.add(about);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(res, stat, connection);
            close(resCount, stat3, null);
            close(null, stat4, null);
        }
        return aboutList;
    }

    private About getAboutWithResultSet(ResultSet res) throws SQLException {
        return new About(res.getInt(1), res.getString(2), res.getInt(3));
    }

    public void save(List<About> aboutList) {
        Connection connection = null;
        Statement statement = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();

            for(About about : aboutList){
                statement.executeUpdate("UPDATE " + DATABASE_NAME + ".about SET text = '" + about.getText()
                        + "', pictureid = " + about.getPictureID() + " WHERE id = " + about.getId() + ";");
            }
            statement.executeUpdate("COMMIT");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(null, statement, connection);
        }
    }
}
