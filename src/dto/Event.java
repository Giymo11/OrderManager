package dto;

import interfaces.Identifiable;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: Patrick
 * Date: 24.03.14
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */
public class Event implements Identifiable {

    private int id;
    private String title;
    private String description;
    private String picture;
    private int pictureid;
    private int priority;

    public String getSQLSetString() {
        return "title = '" + title + "', description = '" + description + "', pictureid = " + pictureid +
                ", priority = " + priority;
    }


    public Event(int id, String title, String description, int pictureid, int priority) {
        setId(id);
        setTitle(title);
        setDescription(description);
        setPictureid(pictureid);
        setPriority(priority);
    }

    public int getPictureid() {
        return pictureid;
    }

    public void setPictureid(int pictureid) {
        this.pictureid = pictureid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + pictureid;
        result = 31 * result + priority;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Event other = (Event) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.picture, other.picture)) {
            return false;
        }
        if (this.priority != other.priority) {
            return false;
        }
        return true;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
