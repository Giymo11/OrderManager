package dto;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 20.01.14
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class Category {
    private int id;
    private String name;

    public Category(int id, String name) {
        setId(id);
        setName(name);
    }

    public String getSQLString() {
        return id + ", '" + name + "'";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
