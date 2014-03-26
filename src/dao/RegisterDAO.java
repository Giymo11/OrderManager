package dao;

import dto.Address;
import dto.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 25.02.14
 * Time: 20:39
 * To change this template use File | Settings | File Templates.
 */
public class RegisterDAO extends JDBCDAO {
    public RegisterDAO(){
        super();
    }

    public void register(User user, Address address,String selectedTown){
        int id = getTownID(selectedTown);
        address.setTownid(id);
        writeAddress(address);
        user.setAdressID(address.getId());
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
                    "', hash = '" + user.getHash() + "', firstname = '" + user.getFirstName() + "', lastname = '" + user.getLastName() +
                    "', telnr = '" + user.getTelNr() + "', addressid = " + user.getAdressID() + " WHERE id = " + user.getId() + ";");
            statement.executeUpdate("COMMIT;");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(null, statement, connection);
        }

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
}
