package dao;

import dto.Town;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Markus
 * Date: 26.02.14
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
public class TownDAO extends JDBCDAO {
    private List<Town> towns;

    public TownDAO(){
        super();
        towns = new ArrayList<>();
    }

    public void read(){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".town;");

            while(resultSet.next())
                towns.add(getTownWithResultSet(resultSet));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
        }
    }

    public void setTowns(List<Town> towns) {
        this.towns = towns;
    }

    public List<Town> getTowns(){
        if(towns.isEmpty())
            read();
        return towns;
    }

    public Town getTownWithID(int id) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Town town = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".town WHERE id = " + id + ";");
            resultSet.next();
            town = getTownWithResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(resultSet, statement, connection);
        }

        return town;
    }

    private Town getTownWithResultSet(ResultSet resultSet) throws SQLException {
        Town town = new Town(resultSet.getInt("PLZ"), resultSet.getString("Name"));
        town.setId(resultSet.getInt("id"));
        return town;
    }

    public void save(int id) {
        Connection connection = null;
        Statement stat = null;
        try {
            connection = getConnection();
            stat = connection.createStatement();
            for (Town town : towns) {
                if (town.getId() == id) {
                    System.out.println(town.getName() + " " + town.getPlz());
                    stat.executeUpdate("UPDATE " + DATABASE_NAME + ".town SET name = '" + town.getName() + "', plz = " + town.getPlz() + " WHERE id = " + id + ";");
                    stat.executeUpdate("COMMIT;");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally{
            close(null, stat, connection);
        }
    }
}
