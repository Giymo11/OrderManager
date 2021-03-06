package beans;

import dao.TownDao;
import dto.Town;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
@RequestScoped
public class TownService {
    private int plz;
    private String name;
    private TownDao townDao;
    private List<String> stringTowns;
    private List<Town> towns;

    public TownService(){
        townDao = new TownDao();
        stringTowns = new ArrayList();
    }

    public List<Town> getTowns() {
        if (towns == null) {
            towns = townDao.getTowns();
        }
        return towns;
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

    public String addTown(){
        Town town = new Town(plz, name);
        townDao.addTown(town);
        towns.add(town);
        plz = 0;
        name = "";
        return "#";
    }

    public String delete() throws SQLException {
        int id = Integer.parseInt(fetchParameter("id"));
        townDao.deleteObject("town", id);
        for(int i = 0; i<towns.size(); i++)
            if(towns.get(i).getId() == id)
                towns.remove(i);
        return "#";
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

    public String getNameWithID(int id){
        return townDao.getTownWithID(id).getName();
    }

    public int getPostalCodeWithID(int id){
        return townDao.getTownWithID(id).getPlz();
    }

    public String save(){
        int id = Integer.parseInt(fetchParameter("idS"));
        townDao.save(towns, id);
        return "#";
    }
}
