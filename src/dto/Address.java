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
    private String street, houseNr;

    public Address(String street, String houseNr) {
        this.street = street;
        this.houseNr = houseNr;
    }

    public Address() {
    }

    public String getSQLString() {
        return id + ", " + street + "', '" + houseNr + "'," + townid;
    }

    public String getHouseNr() {
        return houseNr;
    }

    public void setHouseNr(String houseNr) {
        this.houseNr = houseNr;
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

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", townid=" + townid +
                ", street='" + street + '\'' +
                ", houseNr='" + houseNr + '\'' +
                '}';
    }
}
