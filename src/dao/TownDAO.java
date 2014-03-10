package dao;

import dto.Town;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    }

    public void setTowns(List<Town> towns) {
        this.towns = towns;
    }

    public void writeTown(Town town) {
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

    public List<Town> getTowns(){

        towns = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;


        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * from ordermanager.town;");
            while(resultSet.next()){
                Town town = new Town(resultSet.getInt("PLZ"), resultSet.getString("Name"));
                town.setId(resultSet.getInt("ID"));
                towns.add(town);
            }


        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        finally {
            close(resultSet, statement, connection);
        }

        return towns;

    }




}
