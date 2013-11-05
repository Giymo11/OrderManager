/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import constants.Files;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Sarah
 */

@ManagedBean
public class ContactInfoBean {
    private String name;
    private String street;
    private String location;
    private String telephone;
    private String mail;

    private String mondayAM;
    private String mondayPM;
    private String tuesdayAM;
    private String tuesdayPM;
    private String wednesdayAM;
    private String wednesdayPM;
    private String thursdayAM;
    private String thursdayPM;
    private String fridayAM;
    private String fridayPM;
    private String saturdayAM;
    private String saturdayPM;

    private Properties properties = null;

    public ContactInfoBean() {
        try {
            properties = readProperties();
            assignProperties(properties);
        } catch (FileNotFoundException ex) {
            System.out.println("Property file not found!");
        } catch (IOException ex) {
            Logger.getLogger(ContactInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void assignProperties(Properties properties) {
        name = properties.getProperty("name");
        street = properties.getProperty("street");
        location = properties.getProperty("location");
        telephone = properties.getProperty("telephone");
        mail = properties.getProperty("mail");

        mondayAM = properties.getProperty("mondayAM");
        mondayPM = properties.getProperty("mondayPM");

        tuesdayAM = properties.getProperty("tuesdayAM");
        tuesdayPM = properties.getProperty("tuesdayPM");

        wednesdayAM = properties.getProperty("wednesdayAM");
        wednesdayPM = properties.getProperty("wednesdayPM");

        thursdayAM = properties.getProperty("thursdayAM");
        thursdayPM = properties.getProperty("thursdayPM");

        fridayAM = properties.getProperty("fridayAM");
        fridayPM = properties.getProperty("fridayPM");

        saturdayAM = properties.getProperty("saturdayAM");
        saturdayPM = properties.getProperty("saturdayPM");
    }

    private Properties readProperties() throws IOException {
        InputStreamReader in = new InputStreamReader(new FileInputStream(Files.INFO.getPath()), "UTF-8");
        Properties properties = new Properties();
        properties.load(in);
        in.close();
        return properties;
    }

    public void writeToFile() {
        FacesMessage message = new FacesMessage("New information is stored.");
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(Files.INFO.getPath()), "UTF-8");
            properties.store(out, "Changed Contact-Info");
            out.close();
        } catch (UnsupportedEncodingException e) {
            message = new FacesMessage("Unsupported encoding.");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            message = new FacesMessage("File not found!");
            e.printStackTrace();
        } catch (IOException e) {
            message = new FacesMessage("New information could not be stored.");
            e.printStackTrace();
        } finally {
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        properties.setProperty("name", name);
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
        properties.setProperty("street", street);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        properties.setProperty("location", location);
    }

    public String getTelephone() {
        return telephone;

    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
        properties.setProperty("telephone", telephone);
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
        properties.setProperty("mail", mail);
    }

    public String getMondayAM() {
        return mondayAM;
    }

    public void setMondayAM(String mondayAM) {
        this.mondayAM = mondayAM;
        properties.setProperty("mondayAM", mondayAM);
    }

    public String getMondayPM() {
        return mondayPM;
    }

    public void setMondayPM(String mondayPM) {
        this.mondayPM = mondayPM;
        properties.setProperty("mondayPM", mondayPM);
    }

    public String getTuesdayAM() {
        return tuesdayAM;
    }

    public void setTuesdayAM(String tuesdayAM) {
        this.tuesdayAM = tuesdayAM;
        properties.setProperty("tuesdayAM", tuesdayAM);
    }

    public String getTuesdayPM() {
        return tuesdayPM;
    }

    public void setTuesdayPM(String tuesdayPM) {
        this.tuesdayPM = tuesdayPM;
        properties.setProperty("tuesdayPM", tuesdayPM);
    }

    public String getWednesdayAM() {
        return wednesdayAM;
    }

    public void setWednesdayAM(String wednesdayAM) {
        this.wednesdayAM = wednesdayAM;
        properties.setProperty("wednesdayAM", wednesdayAM);
    }

    public String getWednesdayPM() {
        return wednesdayPM;
    }

    public void setWednesdayPM(String wednesdayPM) {
        this.wednesdayPM = wednesdayPM;
        properties.setProperty("wednesdayPM", wednesdayPM);
    }

    public String getThursdayAM() {
        return thursdayAM;
    }

    public void setThursdayAM(String thursdayAM) {
        this.thursdayAM = thursdayAM;
        properties.setProperty("thursdayAM", thursdayAM);
    }

    public String getThursdayPM() {
        return thursdayPM;
    }

    public void setThursdayPM(String thursdayPM) {
        this.thursdayPM = thursdayPM;
        properties.setProperty("thursdayPM", thursdayPM);
    }

    public String getFridayAM() {
        return fridayAM;
    }

    public void setFridayAM(String fridayAM) {
        this.fridayAM = fridayAM;
        properties.setProperty("fridayAM", fridayAM);
    }

    public String getFridayPM() {
        return fridayPM;
    }

    public void setFridayPM(String fridayPM) {
        this.fridayPM = fridayPM;
        properties.setProperty("fridayPM", fridayPM);
    }

    public String getSaturdayAM() {
        return saturdayAM;
    }

    public void setSaturdayAM(String saturdayAM) {
        this.saturdayAM = saturdayAM;
        properties.setProperty("saturdayAM", saturdayAM);
    }

    public String getSaturdayPM() {
        return saturdayPM;
    }

    public void setSaturdayPM(String saturdayPM) {
        this.saturdayPM = saturdayPM;
        properties.setProperty("saturdayPM", saturdayPM);
    }
}
