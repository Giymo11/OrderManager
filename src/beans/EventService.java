package beans;

import dao.EventDao;
import dto.Event;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
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

    private List<Event> events;
    private List<String> orderEvents;

    public EventService() {
        eventDAO = new EventDao();
    }

    public List<Event> getEvents() {
        if (events == null) {
            events = eventDAO.getEventList();
        }
        return events;
    }

    public void setEvents(List<Event> eventList) {
        events = eventList;
    }

    public List<String> getOrderEvents(){
        if(orderEvents == null) {
            orderEvents = new ArrayList();
            for (Event event : getEvents())
                orderEvents.add(event.getTitle());
        }
        return orderEvents;
    }

    public void setOrderEvents(List<String> list){
        orderEvents = list;
    }

    public void addNewEvent() {
        Event event = eventDAO.addNewEvent(newName, newText, selectedPicture);
        if(event!=null && !newText.equals("") && !newName.equals("")) {
            events.add(event);
            orderEvents.add(newName);
            newName = "";
            newText = "";
        }
        else
            FacesContext.getCurrentInstance().addMessage("Fail", new FacesMessage("Bitte überprüfen Sie Ihre Eingaben"));

    }

    public void delete() {
        int id = Integer.parseInt(fetchParameter("id"));
        for(int i = 0; i<events.size(); i++)
            if(events.get(i).getId() == id)
                events.remove(i);
        eventDAO.delete(id);
        orderEvents = null;
        getOrderEvents();
    }

    public void save(){
        int id = Integer.parseInt(fetchParameter("id"));
        System.out.println("Save called with id: " + id);
        int count=0;
        for(Event event : events){
            if(event.getId() == id) {
                for (Event e : events)
                    if (e.getTitle().equals(event.getTitle()))
                        ++count;

                if (count == 1)
                    eventDAO.save(event);
                else {
                    FacesContext.getCurrentInstance().addMessage("Fail", new FacesMessage("Achtung, zwei gleiche Titel sind nicht erlaubt!"));
                    events = eventDAO.getEventList();
                }
            }
        }
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

    public void saveOrderList(){
        int priority = orderEvents.size()*10;
        for(String str : orderEvents)
            for(Event event : events)
                if(event.getTitle().equals(str)) {
                    event.setPriority(priority);
                    priority-=10;
                }
        eventDAO.writeEventPriorities(events);
        events = eventDAO.getEventList();
    }
}
