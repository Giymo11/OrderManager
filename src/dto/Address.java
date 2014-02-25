package dto;

import interfaces.Identifiable;

/**
 * Created with IntelliJ IDEA.
 * User: Markus
 * Date: 05.02.14
 * Time: 13:28
 * To change this template use File | Settings | File Templates.
 */
public class Address implements Identifiable {
    private int id, plz;
    private String location, street, housNr;

    public Address(int plz, String location, String street, String housNr) {
        this.plz = plz;
        this.location = location;
        this.street = street;
        this.housNr = housNr;
    }

    public String getSQLString() {
        return id + ", " + plz + ", '" + location + "', '" + street + "', '" + housNr + "'" ;
    }

    public String getHousNr() {
        return housNr;
    }

    public void setHousNr(String housNr) {
        this.housNr = housNr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
