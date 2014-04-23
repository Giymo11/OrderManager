package dao;

import dto.Offer;

import javax.faces.bean.SessionScoped;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 19.02.14
 * Time: 20:08
 * To change this template use File | Settings | File Templates.
 */
@SessionScoped
public class OfferDAO extends JdbcDao {
    List<Offer> offerList;

    public OfferDAO(){
        super();
        offerList = new ArrayList();
    }

    public List<Offer> getOfferList(){
        if(offerList.isEmpty())
            read();

        return offerList;
    }

    private void read(){
        Connection con = null;
        Statement stat = null;
        ResultSet res = null;
        ResultSet resPic = null;

        Offer offer;

        try {
            con = getConnection();
            stat = con.createStatement();

            res = stat.executeQuery("SELECT * FROM " + DATABASE_NAME + ".offer");

            while (res.next()) {
                stat = con.createStatement();
                offer = getOfferWithResultSet(res);

                resPic = stat.executeQuery("SELECT name FROM " + DATABASE_NAME + ".picture WHERE pictureid = "
                                    + res.getInt("pictureid") + ";");
                if(resPic.next()) {
                    offer.setPicture(resPic.getString(1));
                }
                offerList.add(offer);

                close(resPic, null, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(res, stat, con);
            close(resPic, null, null);
        }
    }

    private void insertOffer(Offer offer){
        super.insertObject("offer", offer);

        Connection connection = null;
        Statement stat = null;

        try {
            connection = getConnection();
            stat = connection.createStatement();

            stat.executeUpdate("UPDATE " + DATABASE_NAME + ".offer SET " + offer.getSQLSetString() +
                                    " WHERE id = " + offer.getId() + ";");
            stat.executeUpdate("COMMIT");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(null, stat, connection);
        }
    }

    public void addNewOffer(String newName, String text, String selectedPicture){
        Connection con = null;
        Statement statement = null;
        ResultSet res = null;
        Offer offer = null;

        try {
            con = getConnection();
            statement = con.createStatement();
            res = statement.executeQuery("SELECT pictureid FROM " + DATABASE_NAME + ".picture WHERE name = '"
                                                + selectedPicture + "';");
            res.next();

            offer = new Offer(0, newName, text, res.getInt(1), getNewPriority());
            offer.setPicture(selectedPicture);

            offerList.add(offer);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(res, statement, con);
            if(offer!=null)
                insertOffer(offer);
        }
    }

    private int getNewPriority() {
        int priority = 10;

        if (!offerList.isEmpty())
            for (Offer offer : offerList) {
                if (offer.getPriority() >= priority)
                    priority = offer.getPriority() + 10;
            }

        return priority;
    }

    public void delete(int id){
        try {
            for(int i=0; i<offerList.size(); i++)
                if(offerList.get(i).getId() == id)
                    offerList.remove(i);

            super.deleteObject("offer", id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Offer getOfferWithResultSet(ResultSet res) throws SQLException {
        if (res != null)
            return new Offer(res.getInt("ID"),
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
            for (Offer offer : offerList) {
                if (offer.getId() == id) {
                    stat.executeUpdate("UPDATE " + DATABASE_NAME + ".offer SET title = '" + offer.getTitle() +
                            "', description = '" + offer.getDescription() + "' WHERE id = " + id + ";");
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
