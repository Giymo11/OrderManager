package dao;

import dto.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sarah on 03.04.2014.
 */
public class UserAdminDao extends JdbcDao {
    public UserAdminDao(){
        super();
    }

    public List<User> getUserList() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<User> userList = new ArrayList();

        try{
            connection = getConnection();
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".user");

            while(resultSet.next()){
                User u = getUserWithResultSet(resultSet);
                if(!checkAdminRights(u.getId(), connection))
                    userList.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            close(resultSet, statement, connection);
        }
        return userList;
    }

    private boolean checkAdminRights(int id, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet res = statement.executeQuery("SELECT count(*) FROM " + DATABASE_NAME + ".admin WHERE userid = " + id + ";");
        res.next();

        boolean returnValue = false;

        if(res.getInt(1)==1)
            returnValue = true;

        return returnValue;
    }

    private User getUserWithResultSet(ResultSet resultSet) throws SQLException {
        User user = new User(resultSet.getString("Email"),
                resultSet.getString("FirstName"),
                resultSet.getString("LastName"),
                resultSet.getString("hash"),
                resultSet.getString("telnr"),
                resultSet.getDate("birthdate"),
                resultSet.getInt("addressid"),
                resultSet.getBoolean("verified"),
                resultSet.getBoolean("blocked"));
        user.setId(resultSet.getInt("id"));
        return user;
    }

    public void save(int id, List<User> userList) {
        Connection connection = null;
        Statement statement = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();

            for(User user : userList)
                if(user.getId() == id){
                    statement.executeUpdate("UPDATE " + DATABASE_NAME + ".user SET verified = " + user.getVerified() +
                            ", blocked = " + user.isBlocked() + " WHERE id = " + id + ";");
                    statement.executeUpdate("COMMIT;");
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(null, statement, connection);
        }
    }
}
