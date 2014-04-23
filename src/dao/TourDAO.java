package dao;

import dto.Tour;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
public class TourDAO extends JdbcDao {
    private List<Tour> tourList;

    public TourDAO(){
        super();
        tourList = new ArrayList();
        read();
    }

    public List<Tour> getTourList(){
        if(tourList.isEmpty()){
            read();
        }
        return tourList;
    }

    private void read(){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Tour tour = null;
        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".tour;");

            while(resultSet.next()){
                tour = new Tour(resultSet.getDate("Date"));
                tour.setId(resultSet.getInt("ID"));

                if(tour != null)
                    tourList.add(tour);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(resultSet, statement, connection);
        }
    }

    public int getTourIDWithDate(Date date){
        if(!tourList.isEmpty())
            for(Tour tour : tourList)
                if(tour.getDate().equals(date))
                    return tour.getId();

        FacesContext.getCurrentInstance().addMessage("Failure", new FacesMessage("Achtung! Für diesen Tag gibt es noch keine eingetragene Tour"));
        return -1;
    }

    public void addTour(Date date){
        Connection connection = null;
        Statement statement = null;
        Tour newTour = new Tour(date);
        try{
            insertObject("Tour", newTour);
            tourList.add(newTour);

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
    }

    public Date getDateWithID(int tourID) {
        for(Tour tour : tourList)
            if(tour.getId() == tourID)
                return tour.getDate();
        return null;
    }
}
