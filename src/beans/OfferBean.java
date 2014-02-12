package beans;

import dbaccess.ConnectionManager;
import dto.Offer;

import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        offers = new ArrayList<>();
    }

    public List<Offer> getOffers() {
        if (offers.isEmpty())
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

    public void insertOffer(Offer offer) {
        System.out.println("InsertOffer called");
        try {
            connection.createStatement().executeUpdate("INSERT INTO ordermanager.offer VALUES(" + offer.getSQLString() + ");");
            connection.createStatement().executeUpdate("COMMIT;");

            offers.add(offer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNewOffer() {
        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT pictureid FROM ordermanager.picture WHERE Name = '" + selectedPicture + "';");
            res.next();
            int pictureID = res.getInt(1);
            res.close();

            Offer offer = new Offer(++lastID, newName, newText, pictureID, getNewPriority());
            offer.setPicture(selectedPicture);

            newName = "";
            newText = "";

            insertOffer(offer);
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage("Failure", new FacesMessage("Fehler beim Hinzufügen des Angebots!"));
        }
    }

    private int getNewPriority() {
        int priority = 10;

        if (!offers.isEmpty())
            for (Offer offer : offers) {
                if (offer.getPriority() >= priority)
                    priority = offer.getPriority() + 10;
            }

        return priority;
    }

    public void read() throws IOException {
        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT * FROM ordermanager.offer");
            ResultSet resPic = null;
            if (res.next())
                while (true) {
                    Offer offer = getOfferWithResultSet(res);
                    resPic = connection.createStatement().executeQuery("SELECT name FROM ordermanager.picture WHERE pictureid = " + res.getInt("pictureid") + ";");
                    resPic.next();
                    offer.setPicture(resPic.getString(1));
                    offers.add(offer);

                    if (lastID < res.getInt("id")) {
                        lastID = res.getInt("id");
                    }

                    if (res.isLast())
                        break;
                    else
                        res.next();
                }
            res.close();
            resPic.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        System.out.println("Delete called");
        int id = Integer.parseInt(fetchParameter("id"));
        try {
            int indexDel = -1;
            for (int i = 0; i < offers.size(); i++)
                if (offers.get(i).getId() == id)
                    indexDel = i;

            if (indexDel != -1) {
                offers.remove(indexDel);


                connection.createStatement().executeUpdate("DELETE FROM ordermanager.offer WHERE ID = " + id + ";");
                connection.createStatement().executeUpdate("Commit;");
            }
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

    public String getSelectedPicture() {
        return selectedPicture;
    }

    public void setSelectedPicture(String selectedPicture) {
        this.selectedPicture = selectedPicture;
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("OfferBean PreDestroy");
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
