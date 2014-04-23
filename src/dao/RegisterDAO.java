package dao;

import dto.Address;
import dto.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 25.02.14
 * Time: 20:39
 * To change this template use File | Settings | File Templates.
 */
public class RegisterDAO extends JdbcDao {
    public RegisterDAO(){
        super();
    }

    public void register(User user, Address address,String selectedTown){
        int id = getTownID(selectedTown);
        address.setTownid(id);
        if(checkAddressDuplicate(address)) {
            writeAddress(address);
        }
        if(user.getEmail().equals("baeckerei.pock@a1.net"))
            user.setVerified(true);
        user.setAddressID(address.getId());
        writeUser(user);
    }

    private int getTownID(String selectedTown) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select id from " + DATABASE_NAME +  ".town where name = '" + selectedTown + "';");
            resultSet.next();

            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        finally {
            close(resultSet, statement, connection);
        }
        System.out.println("Error: TownID not found");
        return 0;
    }

    private void writeUser(User user) {
        Connection connection = null;
        Statement statement = null;
        try{
            insertObject("user", user);

            connection = getConnection();
            statement = connection.createStatement();

            statement.executeUpdate("UPDATE " + DATABASE_NAME + ".user SET email = '" + user.getEmail() +
                    "', hash = '" + user.getHash() + "', firstname = '" + user.getFirstName() + "', lastname = '"
                    + user.getLastName() + "', telnr = '" + user.getTelNr() + "', addressid = " + user.getAddressID() +
                    ", birthdate = '" + getDateSQL(user.getBirthdate()) + "', verified = " + user.getVerified() +
                    " WHERE id = " + user.getId() + ";");
            statement.executeUpdate("COMMIT;");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(null, statement, connection);
        }
    }

    private String getDateSQL(Date date) {
        if(date.getMonth()<10 && date.getDate()<10)
            return (date.getYear()+1900) + "-0" + (1+date.getMonth()) + "-0" + date.getDate();
        else
        if(date.getMonth()<10)
            return (date.getYear()+1900) + "-0" + (1+date.getMonth()) + "-" + date.getDate();
        if(date.getDate()<10)
            return (date.getYear()+1900) + "-" + (1+date.getMonth()) + "-0" + date.getDate();


        return (date.getYear()+1900) + "-" + (1+date.getMonth()) + "-" + date.getDate();
    }

    private void writeAddress(Address address) {
        Connection connection = null;
        Statement statement = null;

        try{
            insertObject("address", address);

            connection = getConnection();
            statement = connection.createStatement();

            statement.executeUpdate("UPDATE " + DATABASE_NAME + ".address SET street = '" + address.getStreet() +
                    "', HouseNr = '" + address.getHouseNr() + "', townid = " + address.getTownid() +
                    " WHERE id = " + address.getId() + ";");
            statement.executeUpdate("COMMIT;");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(null, statement, connection);
        }
    }

    private boolean checkAddressDuplicate(Address address) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".address");

            while(resultSet.next()){
                if( resultSet.getString("street").equalsIgnoreCase(address.getStreet()) && resultSet.getString("housenr").equalsIgnoreCase(address.getHouseNr()) && resultSet.getInt("townid") == address.getTownid() ){
                    address.setId(resultSet.getInt("id"));
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
        }

        return true;
    }
}
