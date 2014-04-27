/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ContactInfoDao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * @author Sarah
 */

@ManagedBean
@ApplicationScoped
public class ContactInfoBean {
    private ContactInfoDao contactInfoDAO;

    public ContactInfoBean() {
        contactInfoDAO = new ContactInfoDao();
    }

    public String getName() {
        return contactInfoDAO.getName();
    }

    public void setName(String name) {
        contactInfoDAO.setName(name);
    }

    public String getStreet() {
        return contactInfoDAO.getStreet();
    }

    public void setStreet(String street) {
        contactInfoDAO.setStreet(street);
    }

    public String getLocation() {
        return contactInfoDAO.getLocation();
    }

    public void setLocation(String location) {
        contactInfoDAO.setLocation(location);
    }

    public String getTelephone() {
        return contactInfoDAO.getTelephone();
    }

    public void setTelephone(String telephone) {
        contactInfoDAO.setTelephone(telephone);
    }

    public String getMail() {
        return contactInfoDAO.getMail();
    }

    public void setMail(String mail) {
        contactInfoDAO.setMail(mail);
    }

    public String getMondayAM() {
        return contactInfoDAO.getMondayAM();
    }

    public void setMondayAM(String mondayAM) {
        contactInfoDAO.setMondayAM(mondayAM.replace(" ", ""));
    }

    public String getMondayPM() {
        return contactInfoDAO.getMondayPM();
    }

    public void setMondayPM(String mondayPM) {
        contactInfoDAO.setMondayPM(mondayPM.replace(" ", ""));
    }

    public String getTuesdayAM() {
        return contactInfoDAO.getTuesdayAM();
    }

    public void setTuesdayAM(String tuesdayAM) {
        contactInfoDAO.setTuesdayAM(tuesdayAM.replace(" ", ""));
    }

    public String getTuesdayPM() {
        return contactInfoDAO.getTuesdayPM();
    }

    public void setTuesdayPM(String tuesdayPM) {
        contactInfoDAO.setTuesdayPM(tuesdayPM.replace(" ", ""));
    }

    public String getWednesdayAM() {
        return contactInfoDAO.getWednesdayAM();
    }

    public void setWednesdayAM(String wednesdayAM) {
        contactInfoDAO.setWednesdayAM(wednesdayAM.replace(" ", ""));
    }

    public String getWednesdayPM() {
        return contactInfoDAO.getWednesdayPM();
    }

    public void setWednesdayPM(String wednesdayPM) {
        contactInfoDAO.setWednesdayPM(wednesdayPM.replace(" ", ""));
    }

    public String getThursdayAM() {
        return contactInfoDAO.getThursdayAM();
    }

    public void setThursdayAM(String thursdayAM) {
        contactInfoDAO.setThursdayAM(thursdayAM.replace(" ", ""));
    }

    public String getThursdayPM() {
        return contactInfoDAO.getThursdayPM();
    }

    public void setThursdayPM(String thursdayPM) {
        contactInfoDAO.setThursdayPM(thursdayPM.replace(" ", ""));
    }

    public String getFridayAM() {
        return contactInfoDAO.getFridayAM();
    }

    public void setFridayAM(String fridayAM) {
        contactInfoDAO.setFridayAM(fridayAM.replace(" ", ""));
    }

    public String getFridayPM() {
        return contactInfoDAO.getFridayPM();
    }

    public void setFridayPM(String fridayPM) {
        contactInfoDAO.setFridayPM(fridayPM.replace(" ", ""));
    }

    public String getSaturdayAM() {
        return contactInfoDAO.getSaturdayAM();
    }

    public void setSaturdayAM(String saturdayAM) {
        contactInfoDAO.setSaturdayAM(saturdayAM.replace(" ", ""));
    }

    public String getSaturdayPM() {
        return contactInfoDAO.getSaturdayPM();
    }

    public void setSaturdayPM(String saturdayPM) {
        contactInfoDAO.setSaturdayPM(saturdayPM.replace(" ", ""));
    }

    public void writeToDB(){
        contactInfoDAO.writeToDB();
    }
}
