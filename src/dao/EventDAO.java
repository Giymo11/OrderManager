package dao;

import dto.Event;

import javax.faces.bean.SessionScoped;
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
@SessionScoped
public class EventDAO extends JdbcDao {
    List<Event> eventList;

    public EventDAO(){
        super();
        eventList = new ArrayList();
    }

    public List<Event> getEventList(){
        if(eventList.isEmpty())
            read();

        return eventList;
    }

    private void read(){
        Connection con = null;
        Statement stat = null;
        ResultSet res = null;
        ResultSet resPic = null;

        Event event;

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

    public void addNewEvent(String newName, String text, String selectedPicture){
        Connection con = null;
        Statement statement = null;
        ResultSet res = null;
        Event event = null;

        try {
            con = getConnection();
            statement = con.createStatement();
            res = statement.executeQuery("SELECT pictureid FROM " + DATABASE_NAME + ".picture WHERE name = '"
                    + selectedPicture + "';");
            res.next();

            event = new Event(0, newName, text, res.getInt(1), getNewPriority());
            event.setPicture(selectedPicture);

            eventList.add(event);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(res, statement, con);
            if(event!=null)
                insertEvent(event);
        }
    }

    private int getNewPriority() {
        int priority = 10;

        if (!eventList.isEmpty())
            for (Event event : eventList) {
                if (event.getPriority() >= priority)
                    priority = event.getPriority() + 10;
            }

        return priority;
    }

    public void delete(int id){
        try {
            for(int i=0; i<eventList.size(); i++)
                if(eventList.get(i).getId() == id)
                    eventList.remove(i);

            super.deleteObject("event", id);
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
            for (Event event : eventList) {
                if (event.getId() == id) {
                    stat.executeUpdate("UPDATE " + DATABASE_NAME + ".event SET title = '" + event.getTitle() +
                            "', description = '" + event.getDescription() + "' WHERE id = " + id + ";");
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
