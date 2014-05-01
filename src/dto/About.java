package dto;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 17.02.14
 * Time: 14:21
 * To change this template use File | Settings | File Templates.
 */
public class About {
    private int id;
    private String text;
    private int pictureID;

    public About(int id, String text, int pictureID) {
        setId(id);
        setText(text);
        setPictureID(pictureID);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPictureID() {
        return pictureID;
    }

    public void setPictureID(int pictureID) {
        this.pictureID = pictureID;
    }
}
