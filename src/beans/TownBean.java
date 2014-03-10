package beans;

import dao.TownDAO;
import dto.Town;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Markus
 * Date: 26.02.14
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
public class TownBean {
    private int plz;
    private String name;
    private String selectedTown;
    private TownDAO townDAO;
    private List<String> stringTowns;



    public String getSelectedTown() {
        return selectedTown;
    }

    public void setSelectedTown(String selectedTown) {
        this.selectedTown = selectedTown;
    }

    public TownBean(){
        this.townDAO = new TownDAO();
        this.stringTowns = new ArrayList<>();
    }

    public List<Town> getTowns() {
        return townDAO.getTowns();
    }

    public void setTowns(List<Town> towns) {
        townDAO.setTowns(towns);
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTown(){
        Town town = new Town(plz, name);

        townDAO.writeTown(town);
        plz = 0;
        name = "";
    }

    public void delete() throws SQLException {
        int id = Integer.parseInt(fetchParameter("id"));
        townDAO.deleteObject("town", id);
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }

    public List<String> getStringTowns() {
        if(stringTowns.isEmpty()){
            for(Town town : getTowns()){
                stringTowns.add(town.getName());
            }
        }
        return stringTowns;
    }

    public void setStringTowns(List<String> stringTowns) {
        this.stringTowns = stringTowns;
    }
}
