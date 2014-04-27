package dao;

import dto.Offer;

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
public class OfferDao extends JdbcDao {
    public OfferDao(){
        super();
    }

    public List<Offer> getOfferList(){
        Connection con = null;
        Statement stat = null;
        ResultSet res = null;
        ResultSet resPic = null;

        Offer offer;
        List<Offer> offerList = new ArrayList();

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
        return offerList;
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

    public Offer addNewOffer(String newName, String text, String selectedPicture){
        Connection con = null;
        Statement statement = null;
        ResultSet res = null;
        Statement statement1 = null;
        ResultSet resultSet = null;

        Offer offer = null;

        try {
            con = getConnection();
            statement = con.createStatement();
            statement1 = con.createStatement();
            res = statement.executeQuery("SELECT pictureid FROM " + DATABASE_NAME + ".picture WHERE name = '"
                                                + selectedPicture + "';");
            res.next();

            resultSet = statement1.executeQuery("SELECT max(priority) FROM " + DATABASE_NAME + ".offer;");
            resultSet.next();

            offer = new Offer(0, newName, text, res.getInt(1), resultSet.getInt(1)+10);
            offer.setPicture(selectedPicture);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(res, statement, con);
            close(resultSet, statement1, null);
            if(offer!=null)
                insertOffer(offer);
        }

        return offer;
    }

    public void delete(int id){
        try {
            deleteObject("offer", id);
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

    public void save(int id, List<Offer> offerList){
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
