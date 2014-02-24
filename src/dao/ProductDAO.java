package dao;

import dto.Product;

import javax.faces.bean.SessionScoped;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 19.02.14
 * Time: 16:36
 * To change this template use File | Settings | File Templates.
 */
@SessionScoped
public class ProductDAO extends JDBCDAO {
    private List<Product> productList;

    public ProductDAO(){
        super();
        productList = new ArrayList<>();
    }

    public List<Product> getProductList(){
        if(productList.isEmpty())
            read();

        return productList;
    }

    public void read(){
        Connection connection = null;
        Statement statement = null;
        ResultSet res = null;
        ResultSet resCat;
        ResultSet resPic;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery("SELECT * FROM ordermanager.product");

            Product product;
            if (res.next())
                while (true) {
                    statement = connection.createStatement();

                    resCat = statement.executeQuery("SELECT name FROM ordermanager.category WHERE id = " +
                                                    res.getInt("CategoryID") + ";");

                    statement = connection.createStatement();

                    resPic = statement.executeQuery("SELECT name FROM ordermanager.picture WHERE pictureid = "
                                                   + res.getInt(6) + ";");

                    product = getProductWithResultSet(res);

                    resCat.next();
                    resPic.next();

                    product.setCategoryName(resCat.getString(1));
                    product.setPicture(resPic.getString(1));

                    productList.add(product);

                    close(resCat, null, null);
                    close(resPic, null, null);

                    if (res.isLast())
                        break;
                    else
                        res.next();
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(res, statement, connection);
        }
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

    public void insertProduct(Product product){
        insertObject("product", product);

        try {
            Connection con = getConnection();
            Statement stat = con.createStatement();

            stat.executeUpdate("UPDATE ordermanager.product SET " + product.getSQLSetString() +
                    " WHERE id = " + product.getId() + ";");

            stat.executeUpdate("COMMIT;");

            close(null, stat, con);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNewProduct(String selectedCategory, String newName, String newText,
                              float newPrice, String selectedPicture){
        Connection con = null;
        Statement stat = null;
        Statement stat1 = null;
        ResultSet resPicture = null;
        ResultSet resCategory = null;
        Product product = null;

        try {
            con = getConnection();
            stat = con.createStatement();
            stat1 = con.createStatement();

            resPicture = stat.executeQuery("SELECT pictureID FROM ordermanager.picture WHERE name = '" +
                                            selectedPicture + "';");
            resPicture.next();

            resCategory = stat1.executeQuery("SELECT id FROM ordermanager.category WHERE name = '" +
                                            selectedCategory + "';");
            resCategory.next();

            product = new Product(0, resCategory.getInt(1), getNewPriority(),
                                                newName, newText, newPrice, resPicture.getInt(1), true);
            product.setPicture(selectedPicture);
            product.setCategoryName(selectedCategory);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        finally {
            close(resPicture, stat, con);
            close(resCategory, stat1, null);
            productList.add(product);
            if(product!=null)
                insertProduct(product);
        }
    }

    private int getNewPriority() {
        int priority = 10;

        if (!productList.isEmpty())
            for (Product product : productList) {
                if (product.getPriority() >= priority)
                    priority = product.getPriority() + 10;
            }

        return priority;
    }

    public void delete(int id){
        try {
            super.deleteObject("product", id);

            for(int i=0; i<productList.size(); i++)
                if(productList.get(i).getId() == id)
                    productList.remove(i);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public List<Product> getProductsByCategory(String category){
        List<Product> products = new ArrayList<>();

        if(category==null){
            try {
                Connection con = getConnection();
                Statement stat = con.createStatement();
                ResultSet res = stat.executeQuery("SELECT name from ordermanager.category;");
                res.next();
                category=res.getString(1);

                close(res, stat, con);
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        if(productList.isEmpty())
            read();

        try {
            Connection connection = getConnection();
            Statement stat = connection.createStatement();
            ResultSet r = stat.executeQuery("SELECT id FROM ordermanager.category WHERE name = '" + category + "';");
            r.next();

            for(Product p : productList)
                if(p.getCategoryID() == r.getInt(1) && p.isVisible())
                    products.add(p);

            close(r, stat, connection);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return products;
    }

    public void save(int id){
        Connection con = null;
        Statement stat = null;
        try {
            con = getConnection();
            stat = con.createStatement();

            for (Product product : productList) {
                if (product.getId() == id) {
                    stat.executeUpdate("UPDATE ordermanager.product SET name = '" + product.getTitle() +
                            "', description = '" + product.getDescription() + "', visible = " +
                            product.isVisible() + " WHERE id = " + id + ";");

                    stat.executeUpdate("COMMIT;");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(null, stat, con);
        }
    }
}
