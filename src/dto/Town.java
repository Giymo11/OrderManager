package dto;

import interfaces.Identifiable;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 26.02.14
 * Time: 12:20
 * To change this template use File | Settings | File Templates.
 */
public class Town implements Identifiable {
    private int id;
    private int plz;
    private String name;

    public Town(int plz, String name){
        setName(name);
        setPlz(plz);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }
}
