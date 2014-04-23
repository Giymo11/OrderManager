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
public class UserAdminDAO extends JdbcDao {
    private List<User> userList;

    public UserAdminDAO(){
        super();
        userList = new ArrayList();
        read();
    }

    private void read() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".user");

            while(resultSet.next()){
                User u = getUserWithResultSet(resultSet);
                if(!u.getEmail().equalsIgnoreCase("baeckerei.pock@a1.net"))
                    userList.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            close(resultSet, statement, connection);
        }
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

    public List<User> getUserList() {
        return userList;
    }

    public void save(int id) {
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
