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
        ResultSet res = null;
        ResultSet resPic = null;

        Event event;
        List<Event> eventList = new ArrayList();

        try {
            con = getConnection();
            stat = con.createStatement();

            res = stat.executeQuery("SELECT * FROM " + DATABASE_NAME + ".event");

            if(res.next()){
                while (true){
                    stat = con.createStatement();
                    event = getEventWithResultSet(res);

                    resPic = stat.executeQuery("SELECT name FROM " + DATABASE_NAME + ".picture WHERE pictureid = "
                            + res.getInt("pictureid") + ";");
                    resPic.next();

                    event.setPicture(resPic.getString(1));
                    eventList.add(event);

                    close(resPic, null, null);

                    if(res.isLast())
                        break;
                    else
                        res.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(res, stat, con);
            close(resPic, null, null);
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

        Event event = null;

        try {
            con = getConnection();
            statement = con.createStatement();
            statement1 = con.createStatement();
            res = statement.executeQuery("SELECT pictureid FROM " + DATABASE_NAME + ".picture WHERE name = '"
                    + selectedPicture + "';");
            res.next();

            resultSet = statement1.executeQuery("SELECT max(priority) FROM " + DATABASE_NAME + ".event;");
            resultSet.next();

            event = new Event(0, newName, text, res.getInt(1), resultSet.getInt(1));
            event.setPicture(selectedPicture);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(res, statement, con);
            close(resultSet, statement1, null);
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
        if (res != null)
            return new Event(res.getInt("ID"),
                    res.getString("Title"),
                    res.getString("Description"),
                    res.getInt("PictureID"),
                    res.getInt("Priority"));
        return null;
    }

    public void save(int id){
        Connection connection = null;
        Statement stat = null;
        try {
            connection = getConnection();
            stat = connection.createStatement();
            for (Event event : getEventList()) {
                if (event.getId() == id) {
                    stat.executeUpdate("UPDATE " + DATABASE_NAME + ".event SET title = '" + event.getTitle() +
                            "', description = '" + event.getDescription() + "' WHERE id = " + id + ";");
                    stat.executeUpdate("COMMIT;");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            close(null, stat, connection);
        }
    }
}
