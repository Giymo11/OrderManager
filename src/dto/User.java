package dto;

import interfaces.Identifiable;

/**
 * Created with IntelliJ IDEA.
 * User: Markus
 * Date: 24.02.14
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
public class User implements Identifiable {
    private String email, salt, firstName, lastName, hash, telNr;
    private int id, adressID;

    public User(String email, String salt, String firstName, String lastName, String hash, String telNr, int adressID) {
        this.email = email;
        this.salt = salt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hash = hash;
        this.telNr = telNr;
        this.adressID = adressID;
    }

    public String getSQLString() {
        return id + ", '" + email + "', '" + salt + "', '" + hash + "', " +
                "'" + firstName +  "', '" + lastName + "', '"+ telNr + "'," + adressID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTelNr() {
        return telNr;
    }

    public void setTelNr(String telNr) {
        this.telNr = telNr;
    }

    public int getAdressID() {
        return adressID;
    }

    public void setAdressID(int adress) {
        this.adressID = adress;
    }

}
