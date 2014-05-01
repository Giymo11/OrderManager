package dao;

import dto.Tour;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 26.02.14
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
public class TourDao extends JdbcDao {
    public TourDao(){
        super();
    }

    public List<Tour> getTourList(){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Tour tour = null;
        List<Tour> tourList = new ArrayList();
        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".tour;");

            while(resultSet.next()){
                tour = new Tour(resultSet.getDate("Date"));
                tour.setId(resultSet.getInt("ID"));

                tourList.add(tour);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(resultSet, statement, connection);
        }
        return tourList;
    }

    public Tour addTour(Date date){
        Connection connection = null;
        Statement statement = null;
        Tour newTour = new Tour(date);
        try{
            insertObject("Tour", newTour);

            connection = getConnection();
            statement = connection.createStatement();
            statement.executeUpdate("UPDATE " + DATABASE_NAME + ".tour SET date = '" + (1900+date.getYear()) + "-" + (date.getMonth()+1) + "-"
                    + date.getDate() + "' WHERE id = " + newTour.getId() + ";");
            statement.executeUpdate("COMMIT;");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(null, statement, connection);
        }
        return newTour;
    }


}
