package dao;

import dto.Event;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Patrick
 * Date: 17.03.14
 * Time: 15:24
 * To change this template use File | Settings | File Templates.
 */
public class EventDao extends JdbcDao {
    public EventDao(){
        super();
    }

    public List<Event> getEventList(){
        Connection con = null;
        Statement stat = null;
        Statement statement = null;
        ResultSet res = null;
        ResultSet resPic = null;

        Event event;
        List<Event> eventList = new ArrayList();

        try {
            con = getConnection();
            stat = con.createStatement();
            statement = con.createStatement();
            res = stat.executeQuery("SELECT * FROM " + DATABASE_NAME + ".event ORDER BY priority desc;");

            while (res.next()) {
                event = getEventWithResultSet(res);

                resPic = statement.executeQuery("SELECT name FROM " + DATABASE_NAME + ".picture WHERE pictureid = "
                        + res.getInt("pictureid") + ";");
                if(resPic.next()) {
                    event.setPicture(resPic.getString(1));
                }
                eventList.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(res, stat, con);
            close(resPic, statement, null);
        }
        return eventList;
    }

    private void insertEvent(Event event){
        super.insertObject("event", event);

        Connection connection = null;
        Statement stat = null;

        try {
            connection = getConnection();
            stat = connection.createStatement();

            stat.executeUpdate("UPDATE " + DATABASE_NAME + ".event SET " + event.getSQLSetString() +
                    " WHERE id = " + event.getId() + ";");
            stat.executeUpdate("COMMIT");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(null, stat, connection);
        }
    }

    public Event addNewEvent(String newName, String text, String selectedPicture){
        Connection con = null;
        Statement statement = null;
        ResultSet res = null;
        Statement statement1 = null;
        ResultSet resultSet = null;
        Statement statement2 = null;
        ResultSet resultSet1 = null;

        Event event = null;

        try {
            con = getConnection();
            statement2 = con.createStatement();
            resultSet1 = statement2.executeQuery("SELECT count(*) FROM " + DATABASE_NAME + ".event WHERE title = '"
                    + newName + "';");
            if(resultSet1.next()){
                if(resultSet1.getInt(1)>0)
                    return null;
            }

            statement = con.createStatement();
            statement1 = con.createStatement();
            res = statement.executeQuery("SELECT pictureid FROM " + DATABASE_NAME + ".picture WHERE name = '"
                    + selectedPicture + "';");
            res.next();

            resultSet = statement1.executeQuery("SELECT max(priority) FROM " + DATABASE_NAME + ".event;");
            resultSet.next();

            event = new Event(0, newName, text, res.getInt(1), resultSet.getInt(1)+10);
            event.setPicture(selectedPicture);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(res, statement, con);
            close(resultSet, statement1, null);
            close(resultSet1, statement2, null);
            if(event!=null)
                insertEvent(event);
        }
        return event;
    }

    public void delete(int id){
        try {
            deleteObject("event", id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Event getEventWithResultSet(ResultSet res) throws SQLException {
        return new Event(res.getInt("ID"),
                res.getString("Title"),
                res.getString("Description"),
                res.getInt("PictureID"),
                res.getInt("Priority"));
    }

    public void save(Event event){
        Connection connection = null;
        Statement stat = null;
        try {
            connection = getConnection();
            stat = connection.createStatement();

            stat.executeUpdate("UPDATE " + DATABASE_NAME + ".event SET title = '" + event.getTitle() +
                    "', description = '" + event.getDescription() + "' WHERE id = " + event.getId() + ";");
            stat.executeUpdate("COMMIT;");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            close(null, stat, connection);
        }
    }

    public void writeEventPriorities(List<Event> events) {
        Connection connection = null;
        Statement statement = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM " + DATABASE_NAME + ".event;");
            statement.executeUpdate("COMMIT;");
            close(null, statement, connection);

            for(Event event : events) {
                insertObject("event", event);
                connection = getConnection();
                statement = connection.createStatement();
                statement.executeUpdate("UPDATE " + DATABASE_NAME + ".event SET priority = " + event.getPriority() +
                        ", title = '" + event.getTitle() + "', description = '" + event.getDescription() +
                        "', pictureID = " + event.getPictureid() + " WHERE id = " + event.getId() + ";");
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
