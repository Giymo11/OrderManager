package dao;

import dto.Address;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 19.03.14
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */
public class AddressDAO extends JDBCDAO {
    public AddressDAO(){
        super();
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

    private Address getAddressWithResultSet(ResultSet resultSet) throws SQLException {
        Address a = new Address(resultSet.getString("street"),
                resultSet.getString("HouseNr"));
        a.setId(resultSet.getInt("id"));
        a.setTownid(resultSet.getInt("townid"));
        return a;
    }
}
