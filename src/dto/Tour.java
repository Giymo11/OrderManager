package dto;

import interfaces.Identifiable;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 26.02.14
 * Time: 11:08
 * To change this template use File | Settings | File Templates.
 */
public class Tour implements Identifiable {
    private int id;
    private Date date;

    public Tour(Date date){
        setDate(date);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
