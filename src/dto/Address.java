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
    private int id, townid;
    private String street, housNr;

    public Address(String street, String housNr) {
        this.street = street;
        this.housNr = housNr;
    }

    public String getSQLString() {
        return id + ", " + street + "', '" + housNr + "'," + townid;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getTownid() {
        return townid;
    }

    public void setTownid(int townid) {
        this.townid = townid;
    }
}
