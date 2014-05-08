package beans;

import dao.EventDao;
import dto.Event;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
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
@ApplicationScoped
public class EventService {
    private EventDao eventDAO;

    private String newText;
    private String newName;
    private String selectedPicture;
    private int newPriority;

    private List<Event> events;

    public EventService() {
        eventDAO = new EventDao();
    }

    public List<Event> getEvents() {
        if (events == null) {
            events = eventDAO.getEventList();
        }
        return events;
    }

    public String addNewEvent() {
        events.add(eventDAO.addNewEvent(newName, newText, selectedPicture));

        newName = "";
        newText = "";
        return "#";
    }

    public String delete() {
        int id = Integer.parseInt(fetchParameter("id"));
        for(int i = 0; i<events.size(); i++)
            if(events.get(i).getId() == id)
                events.remove(i);
        eventDAO.delete(id);
        return "#";
    }

    public String save(){
        int id = Integer.parseInt(fetchParameter("ids"));
        eventDAO.save(id);
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

    public void setNewPriority(int newPriority){
        if(eventDAO.isPriorityAlreadyInDB(newPriority))
            eventDAO.reorganisePriorities();
        this.newPriority = newPriority;
    }

    public int getNewPriority(){
        return newPriority;
    }
}
