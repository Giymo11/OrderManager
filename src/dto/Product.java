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
    private int pictureID;
    private boolean visible;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (categoryID != product.getCategoryID()) return false;
        if (id != product.getId()) return false;
        if (pictureID != product.getPictureID()) return false;
        if (Float.compare(product.getPrice(), price) != 0) return false;
        if (priority != product.getPriority()) return false;
        if (visible != product.isVisible()) return false;
        if (description != null ? !description.equals(product.getDescription()) : product.getDescription() != null) return false;
        if (title != null ? !title.equals(product.getTitle()) : product.getTitle() != null) return false;

        return true;
    }

    public int getPictureID() {
        return pictureID;
    }

    public void setPictureID(int pictureID) {
        this.pictureID = pictureID;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
