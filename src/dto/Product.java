package dto;

import interfaces.Identifiable;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 20.01.14
 * Time: 13:34
 * Constructor: int id, int categoryID, int priority, String title, String description, float price, String picture
 */
public class Product implements Identifiable{
    private int id;
    private int categoryID;
    private int priority;
    private String title;
    private String description;
    private float price;
    private String picture;
    private int pictureID;
    private String categoryName;
    private boolean visible;

    public String getSQLSetString() {
        return "categoryid = " + categoryID + ", name =  '" + title + "', description = '" + description + "', price = " +
                price + ", pictureid = " + pictureID + ", priority = " + priority + ", visible = " + visible;
    }

    public Product(int id, int categoryID, int priority, String title, String description, float price, int pictureID, boolean visible) {
        setId(id);
        setCategoryID(categoryID);
        setTitle(title);
        setDescription(description);
        this.pictureID = pictureID;
        setPriority(priority);
        setPrice(price);
        setVisible(visible);
    }

    public boolean equals(Product product) {
        if (id != product.getId())
            return false;
        if (categoryID != product.getCategoryID())
            return false;
        if (priority != product.getPriority())
            return false;
        if (!title.equals(product.getTitle()))
            return false;
        if (!description.equals(product.getDescription()))
            return false;
        if (price != product.getPrice())
            return false;
        if (!picture.equals(product.getPicture()))
            return false;
        if (visible != product.isVisible())
            return false;

        return true;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
