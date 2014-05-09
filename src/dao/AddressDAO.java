package dao;

import dto.Address;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 19.03.14
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */
public class AddressDao extends JdbcDao {
    public AddressDao(){
        super();
    }

    public void writeAddress(Address address) {
        System.out.println("write address called, " + address.getStreet());
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

    public Address getAddressWithID(int id){
        Address address = null;

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".address WHERE id = " + id + ";");
            resultSet.next();
            address = getAddressWithResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            close(resultSet, statement, connection);
        }

        return address;
    }

    public List getAddressesWithTownID(int id){
        List addresses = new ArrayList();
        Address address = null;

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".address WHERE townid = " + id + ";");
            while(resultSet.next()){
                addresses.add(getAddressWithResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            close(resultSet, statement, connection);
        }

        return addresses;
    }

    private Address getAddressWithResultSet(ResultSet resultSet) throws SQLException {
        Address a = new Address(resultSet.getString("street"),
                resultSet.getString("HouseNr"));
        a.setId(resultSet.getInt("id"));
        a.setTownid(resultSet.getInt("townid"));
        return a;
    }
}
