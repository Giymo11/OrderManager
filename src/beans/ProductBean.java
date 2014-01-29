package beans;

import dbaccess.ConnectionManager;
import dto.Product;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 20.01.14
 * Time: 13:30
 */

@ManagedBean
public class ProductBean {
    private List<Product> products;

    private String newText;
    private String newName;
    private int newCategoryID;
    private float newPrice;
    private String newPicture;
    private List<Product> selectedCatProducts;

    private int lastID = 0;
    private ConnectionManager connectionManager;
    private Connection connection;

    public ProductBean() {
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("jdbc/dataSource", false);
        products = new ArrayList<>();
        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() throws IOException {
        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT * FROM ordermanager.product");
            ResultSet resCat;
            Product product;
            while (res.next()) {
                resCat = connection.createStatement().executeQuery("SELECT name FROM ordermanager.category WHERE id = " + res.getInt("CategoryID") + ";");

                product = getProductWithResultSet(res);
                resCat.next();
                product.setCategoryName(resCat.getString(1));

                if (product != null)
                    products.add(product);

                if (lastID < res.getInt(1)) {
                    lastID = res.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertProduct(Product product) {
        try {
            connection.createStatement().executeUpdate("INSERT INTO ordermanager.product VALUES(" + product.getSQLString() + ");");
            connection.createStatement().executeUpdate("COMMIT;");

            products.add(product);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getProducts() {
        if (products == null)
            try {
                read();
            } catch (IOException e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("Failed to get offers from database"));
            }
        return products;
    }

    public void delete() {
        int id = Integer.parseInt(fetchParameter("id"));
        ResultSet res;
        Product product = null;

        try {
            Statement statement = connection.createStatement();
            res = statement.executeQuery("SELECT * FROM ordermanager.product WHERE ID = " + id + ";");

            if (res.next())
                product = getProductWithResultSet(res);

            if (product != null) {
                products.remove(product);
                connection.createStatement().executeUpdate("DELETE FROM ordermanager.product WHERE ID = " + id + ";");
                connection.createStatement().executeUpdate("COMMIT;");
            }

            statement.close();
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }

    private Product getProductWithResultSet(ResultSet res) throws SQLException {
        return new Product(res.getInt(1),
                res.getInt(2),
                res.getInt(7),
                res.getString(3),
                res.getString(4),
                res.getFloat(5),
                res.getInt(6));
    }

    public void addNewProduct() throws SQLException {
        System.out.println("Add new Product");
        ResultSet res;
        Statement statement = connection.createStatement();
        res = statement.executeQuery("SELECT name FROM ordermanager.category WHERE id = " + newCategoryID + ";");
        res.next();

        int prio = getNewPriority();
        System.out.println("New Priority: " + prio);

        int picId = new PictureBean().getIDWithString(newPicture);
        System.out.println("New PictureId: " + picId);

        Product product = new Product(++lastID, newCategoryID, prio, newName, newText, newPrice, picId);
        System.out.println(product.getSQLString());
        product.setCategoryName(res.getString(1));

        insertProduct(product);
        res.close();
        statement.close();
    }

    private int getNewPriority() {
        int priority = 10;

        if (!products.isEmpty())
            for (Product product : products) {
                if (product.getPriority() >= priority)
                    priority = product.getPriority() + 10;
            }

        return priority;
    }

    public void getProductsByCategory(int categoryID) {
        selectedCatProducts = new ArrayList();

        for (Product product : products)
            if (product.getCategoryID() == categoryID)
                selectedCatProducts.add(product);

        if (selectedCatProducts.isEmpty())
            FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("Keine Produkte in dieser Kategorie"));
    }

    public void selectCategory(ValueChangeEvent event) {
        String name = (String) event.getNewValue();
        try {
            Statement statement = connection.createStatement();
            ResultSet res;
            res = statement.executeQuery("SELECT id from ordermanager.category WHERE name = '" + name.toUpperCase() + "';");
            res.next();
            setNewCategoryID(res.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectPicture(ValueChangeEvent event) {
        newPicture = (String) event.getNewValue();
    }

    public String getNewText() {
        return newText;
    }

    public void setNewText(String newText) {
        this.newText = newText;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public void setNewCategoryID(int newCategoryID) {
        this.newCategoryID = newCategoryID;
    }

    public float getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(float newPrice) {
        System.out.println("Price: " + newPrice);
        this.newPrice = newPrice;
    }

    public List<Product> getSelectedCatProducts() {
        return selectedCatProducts;
    }

    public void setSelectedCatProducts(List<Product> selectedCatProducts) {
        this.selectedCatProducts = selectedCatProducts;
    }
}
