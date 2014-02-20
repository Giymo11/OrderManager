package beans;

import dao.OfferDAO;
import dto.Offer;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
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
    private OfferDAO offerDAO;

    private String newText;
    private String newName;
    private String selectedPicture;

    public OfferBean() {
        offerDAO = new OfferDAO();
    }

    public List<Offer> getOffers() {
        return offerDAO.getOfferList();
    }

    public void addNewOffer() {
       offerDAO.addNewOffer(newName, newText, selectedPicture);

       newName = "";
       newText = "";
    }

    public void delete() {
        int id = Integer.parseInt(fetchParameter("id"));
        offerDAO.delete(id);
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
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
}
