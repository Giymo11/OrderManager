package dao;

import dto.Address;
import dto.Town;
import dto.User;

import java.sql.Connection;
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

    public void register(User user, Address address, Town town) {
        writeTown(town);
        address.setTownid(town.getId());
        writeAddress(address);
        user.setAdressID(address.getId());
        writeUser(user);
    }

    private void writeTown(Town town) {
        Connection connection = null;
        Statement statement = null;
        try{
            insertObject("town", town);

            connection = getConnection();
            statement = connection.createStatement();

            statement.executeUpdate("UPDATE ordermanager.town SET plz = " + town.getPlz() +
                    ", name = '" + town.getName() + "' WHERE id = " + town.getId() + ";");
            statement.executeUpdate("COMMIT;");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(null, statement, connection);
        }
    }

    private void writeUser(User user) {
        Connection connection = null;
        Statement statement = null;
        try{
            insertObject("user", user);

            connection = getConnection();
            statement = connection.createStatement();

            statement.executeUpdate("UPDATE ordermanager.user SET email = '" + user.getEmail() + "', salt= '" + user.getSalt() +
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

            statement.executeUpdate("UPDATE ordermanager.address SET street = '" + address.getStreet() +
                    "', HouseNr = '" + address.getHousNr() + "', townid = " + address.getTownid() +
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
