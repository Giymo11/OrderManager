package dto;

import interfaces.Identifiable;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Markus
 * Date: 24.02.14
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
public class User implements Identifiable {
    private String email, firstName, lastName, hash, telNr;
    private int id, addressID;
    private Date birthdate;
    private boolean verified, blocked;

    public User(String email, String firstName, String lastName, String hash, String telNr, Date birthdate, int addressID, boolean verified, boolean blocked) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hash = hash;
        this.telNr = telNr;
        this.addressID = addressID;
        setBirthdate(birthdate);
        setVerified(verified);
        setBlocked(blocked);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int adress) {
        this.addressID = adress;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public boolean getVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
