package beans;

import dto.Event;
import dao.EventDAO;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Patrick
 * Date: 17.03.14
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@SessionScoped
public class EventBean {

    private EventDAO eventDAO;

    private String newText;
    private String newName;
    private String selectedPicture;

    public EventBean() {
        eventDAO = new EventDAO();
    }

    public List<Event> getEvents() {
        return eventDAO.getEventList();
    }

    public void addNewEvent() {
        eventDAO.addNewEvent(newName, newText, selectedPicture);

        newName = "";
        newText = "";
    }

    public void delete() {
        int id = Integer.parseInt(fetchParameter("id"));
        eventDAO.delete(id);
    }

    public void save(){
        int id = Integer.parseInt(fetchParameter("ids"));
        eventDAO.save(id);
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
