package beans;

import dbaccess.ConnectionManager;
import dto.Offer;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Giymo11
 * Date: 04.11.13
 * Time: 13:14
 * Represents the backing-bean of the offers.xhtml site.
 */
@ManagedBean
public class OfferBean {
    private List<Offer> offers;
    private String newText;
    private String newName;
    private String selectedPicture;
    private int lastID = 0;
    private ConnectionManager connectionManager;
    private Connection connection;

    public OfferBean() {
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("jdbc/dataSource", false);
        offers = null;
    }

    public List<Offer> getOffers() {
        if (offers == null)
            try {
                read();
            } catch (IOException e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("Failed to get offers from database"));
            }
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public void insertOffer(Offer offer) throws IOException {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO ordermanager.offer VALUES(" + offer.getSQLString() + ");");
            connection.createStatement().executeUpdate("COMMIT;");
            offers.add(offer);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNewOffer() throws IOException {
        insertOffer(new Offer(++lastID, newName, newText, new PictureBean().getIDWithString(selectedPicture), getNewPriority()));
    }

    private int getNewPriority() {
        int priority = 10;

        for (Offer offer : offers) {
            if (offer.getPriority() >= priority)
                priority = offer.getPriority() + 10;
        }

        return priority;
    }

    public void read() throws IOException {
        offers = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM ordermanager.offer");

            while (res.next()) {
                offers.add(getOfferWithResultSet(res));
                if (lastID < res.getInt("id")) {
                    lastID = res.getInt("id");
                }
            }
            statement.close();
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        ResultSet res;
        Offer offer = null;
        int id = Integer.parseInt(fetchParameter("id"));
        try {
            Statement statement = connection.createStatement();
            res = statement.executeQuery("SELECT * FROM ordermanager.offer WHERE ID = " + id + ";");
            if (res.next())
                offer = getOfferWithResultSet(res);
            if (offer != null) {
                offers.remove(offer);

                connection.createStatement().executeUpdate("DELETE FROM ordermanager.offer WHERE ID = " + id + ";");
                connection.createStatement().executeUpdate("Commit;");
            }
            statement.close();
            res.close();
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

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }

    public void selectPicture(ValueChangeEvent event) {
        selectedPicture = (String) event.getNewValue();

    }

    public String getNewText() {
        return newText;
    }

    public void setNewText(String newText) {
        this.newText = newText;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
