package beans;

import dbaccess.ConnectionManager;
import dto.Product;

import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
@ViewScoped
public class ProductBean {
    private List<Product> products;

    private String newText;
    private String newName;
    private float newPrice;
    private List<Product> selectedCatProducts;

    private String selectedCategory;
    private String selectedPicture;

    private int lastID = 0;
    private ConnectionManager connectionManager;
    private Connection connection;

    public ProductBean() {
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("jdbc/dataSource", false);
        products = new ArrayList<>();
        selectedCatProducts = new ArrayList<>();
    }

    public void read() throws IOException {
        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT * FROM ordermanager.product");
            ResultSet resCat;
            ResultSet resPic;
            Product product;
            if (res.next())
                while (true) {
                    resCat = connection.createStatement().executeQuery("SELECT name FROM ordermanager.category WHERE id = " + res.getInt("CategoryID") + ";");
                    resPic = connection.createStatement().executeQuery("SELECT name FROM ordermanager.picture WHERE pictureid = " + res.getInt(6) + ";");

                    product = getProductWithResultSet(res);
                    resCat.next();
                    resPic.next();
                    product.setCategoryName(resCat.getString(1));
                    product.setPicture(resPic.getString(1));

                    products.add(product);

                    if (lastID < res.getInt(1)) {
                        lastID = res.getInt(1);
                    }
                    if (res.isLast())
                        break;
                    else
                        res.next();
                }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertProduct(Product product) {
        try {
            connection.createStatement().execute("INSERT INTO ordermanager.product VALUES(" + product.getSQLString() + ")");
            connection.createStatement().executeUpdate("COMMIT;");

            products.add(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getProducts() {
        if (products.isEmpty())
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
        try {
            int indexDel = -1;
            for (int i = 0; i < products.size(); i++)
                if (products.get(i).getId() == id)
                    indexDel = i;

            if (indexDel != -1) {
                products.remove(indexDel);

                connection.createStatement().executeUpdate("DELETE FROM ordermanager.product WHERE ID = " + id + ";");
                connection.createStatement().executeUpdate("COMMIT;");
            }

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
                res.getInt(6),
                res.getBoolean(8));
    }

    public void addNewProduct() {
        try {
            ResultSet res;

            res = connection.createStatement().executeQuery("SELECT id FROM ordermanager.category WHERE name = '" + selectedCategory + "';");
            res.next();

            ResultSet resPic = connection.createStatement().executeQuery("SELECT pictureID FROM ordermanager.picture WHERE name = '" + selectedPicture + "';");
            resPic.next();

            Product product = new Product(++lastID, res.getInt(1), getNewPriority(), newName, newText, newPrice, resPic.getInt(1), true);
            product.setCategoryName(selectedCategory);

            res.close();

            newName = "";
            newText = "";
            newPrice = 0.0f;

            insertProduct(product);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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

    public void getProductsByCategory() {
        selectedCatProducts = new ArrayList();
        try {
            if (products.isEmpty())
                read();

            ResultSet res = connection.createStatement().executeQuery("SELECT id FROM ordermanager.category WHERE name = '" + selectedCategory + "';");

            res.next();

            for (Product product : products)
                if (product.getCategoryID() == res.getInt(1))
                    if (product.isVisible())
                        selectedCatProducts.add(product);

            if (selectedCatProducts.isEmpty())
                FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("Keine Produkte in dieser Kategorie"));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Product getProductByID(int id) {
        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT * FROM ordermanager.product WHERE ID = " + id + ";");
            return getProductWithResultSet(res);
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage("Failure", new FacesMessage("Failed to get Product with productID: " + id));
        }
        return null;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
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

    public float getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(float newPrice) {
        this.newPrice = newPrice;
    }

    public List<Product> getSelectedCatProducts() {
        if (selectedCatProducts.isEmpty()) {
            try {
                ResultSet res = connection.createStatement().executeQuery("SELECT name FROM ordermanager.category");
                res.next();
                selectedCategory = res.getString(1);
                getProductsByCategory();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return selectedCatProducts;
    }

    public void setSelectedCatProducts(List<Product> selectedCatProducts) {
        this.selectedCatProducts = selectedCatProducts;
    }

    public String getSelectedPicture() {
        return selectedPicture;
    }

    public void setSelectedPicture(String selectedPicture) {
        this.selectedPicture = selectedPicture;
    }

    @PreDestroy
    public void preDestroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void save() {
        int id = Integer.parseInt(fetchParameter("idS"));
        try {
            for (Product product : products) {
                if (product.getId() == id) {
                    connection.createStatement().executeUpdate("UPDATE ordermanager.product SET name = '" +
                            product.getTitle() + "', description = '" + product.getDescription() + "', visible = " +
                            product.isVisible() + " WHERE id = " + id + ";");
                    connection.createStatement().executeUpdate("COMMIT;");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
