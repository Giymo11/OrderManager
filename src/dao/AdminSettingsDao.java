package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Sarah on 27.04.2014.
 */
public class AdminSettingsDao extends JdbcDao {
    public AdminSettingsDao(){
        super();
    }

    public void addAsAdmin(int id) {
        Connection connection = null;
        Statement statement = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();

            statement.executeUpdate("INSERT INTO " + DATABASE_NAME + ".admin VALUES(" + id + ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            close(null, statement, connection);
        }
    }
}
