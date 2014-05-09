package beans;

import dao.OfferDao;
import dto.Offer;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
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
@ApplicationScoped
public class OfferService {
    private OfferDao offerDao;

    private String newText;
    private String newName;
    private String selectedPicture;

    private List<Offer> offerList;
    private List<String> orderOffers;

    public OfferService() {
        offerDao = new OfferDao();
    }

    public List<Offer> getOffers() {
        if (offerList == null) {
            offerList = offerDao.getOfferList();
        }
        return offerList;
    }

    public String addNewOffer() {
        offerList.add(offerDao.addNewOffer(newName, newText, selectedPicture));
        orderOffers = null;
        getOrderOffers();
        newName = "";
        newText = "";
        return "#";
    }

    public String delete() {
        int id = Integer.parseInt(fetchParameter("id"));
        for(int i = 0; i<offerList.size(); i++)
            if(offerList.get(i).getId() == id)
                offerList.remove(i);
        offerDao.delete(id);
        orderOffers = null;
        getOrderOffers();
        return "#";
    }

    public String save(){
        int id = Integer.parseInt(fetchParameter("ids"));
        offerDao.save(id, offerList);
        return "#";
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

    public List<String> getOrderOffers(){
        if(orderOffers == null) {
            orderOffers = new ArrayList();
            for (Offer offer : getOffers())
                orderOffers.add(offer.getTitle());
        }
        return orderOffers;
    }

    public void setOrderOffers(List<String> list){
        orderOffers = list;
    }

    public void saveOrderList() {
        int priority = orderOffers.size() * 10;
        for (String str : orderOffers)
            for (Offer offer : offerList)
                if (offer.getTitle().equals(str)) {
                    offer.setPriority(priority);
                    priority -= 10;
                }
        offerDao.writeOfferPriorities(offerList);
        offerList = offerDao.getOfferList();
    }
}
