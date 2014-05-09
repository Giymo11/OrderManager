package dao;

import dto.Product;

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
public class ProductDao extends JdbcDao {
    public ProductDao(){
        super();
    }

    public List<Product> getProductList(){
        Connection connection = null;
        Statement statement = null;
        ResultSet res = null;

        List<Product> productList = new ArrayList();
        try {
            connection = getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".product ORDER BY priority desc");

            Product product;
            while (res.next()) {
                product = getProductWithResultSet(res);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(res, statement, connection);
        }
        return productList;
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

            stat.executeUpdate("UPDATE " + DATABASE_NAME + ".product SET name = '" + product.getTitle() +
                    "', categoryid = " + product.getCategoryID() + ", description = '" + product.getDescription() +
                    "', price = " + product.getPrice() + ", pictureID = " + product.getPictureID() + ", priority = " +
                    product.getPriority() + ", visible = " + product.isVisible() +
                    " WHERE id = " + product.getId() + ";");

            stat.executeUpdate("COMMIT;");

            close(null, stat, con);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product addNewProduct(String selectedCategory, String newName, String newText,
                              float newPrice, String selectedPicture){
        Connection con = null;
        Statement stat = null;
        Statement stat1 = null;
        Statement statPriority = null;
        Statement statName = null;
        ResultSet resPicture = null;
        ResultSet resCategory = null;
        ResultSet resPriority = null;
        ResultSet resName = null;

        Product product = null;

        try {
            con = getConnection();
            statName = con.createStatement();
            resName = statName.executeQuery("SELECT count(*) FROM " + DATABASE_NAME + ".product WHERE name = '" + newName + "';");
            resName.next();
            if(resName.getInt(1)==0) {
                stat = con.createStatement();
                stat1 = con.createStatement();
                statPriority = con.createStatement();

                resPicture = stat.executeQuery("SELECT pictureID FROM " + DATABASE_NAME + ".picture WHERE name = '" +
                        selectedPicture + "';");
                resPicture.next();

                resCategory = stat1.executeQuery("SELECT id FROM " + DATABASE_NAME + ".category WHERE name = '" +
                        selectedCategory + "';");

                resPriority = statPriority.executeQuery("SELECT max(priority) FROM " + DATABASE_NAME + ".product");
                resPriority.next();

                if (resCategory.next() && resPicture.isFirst()) {
                    product = new Product(0, resCategory.getInt(1), resPriority.getInt(1) + 10,
                            newName, newText, newPrice, resPicture.getInt(1), true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        finally {
            close(resPicture, stat, con);
            close(resCategory, stat1, null);
            close(resPriority, statPriority, null);
            close(resName, statName, null);
            if(product!=null)
                insertProduct(product);
        }
        return product;
    }

    public void delete(int id){
        try {
            super.deleteObject("product", id);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public List<Product> getProductsForCategory(String category){
        List<Product> products = new ArrayList();

        if(category == null)
            category = getFirstCategory();
        Connection connection = null;
        Statement stat = null;
        ResultSet resultSet = null;
        ResultSet resPicture = null;
        Statement statPicture = null;

        try {
            connection = getConnection();
            stat = connection.createStatement();
            statPicture = connection.createStatement();
            resultSet = stat.executeQuery("SELECT * FROM " + DATABASE_NAME + ".product WHERE categoryid = " +
                    " (SELECT id FROM " + DATABASE_NAME + ".category WHERE name = '" + category + "') ORDER BY priority desc;");

            while(resultSet.next()){
                Product product = getProductWithResultSet(resultSet);

                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally{
            close(resultSet, stat, connection);
            close(resPicture, statPicture, null);
        }
        return products;
    }

    private String getFirstCategory() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String category = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT name FROM " + DATABASE_NAME + ".category");

            if(resultSet.next())
                category = resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
        }
        return category;
    }

    public boolean save(Product product){
        Connection con = null;
        Statement stat = null;
        ResultSet resultSet = null;
        try {
            con = getConnection();
            stat = con.createStatement();
            resultSet = stat.executeQuery("SELECT count(*) FROM " + DATABASE_NAME + ".product WHERE name = '" + product + "';");
            resultSet.next();
            if (resultSet.getInt(1) == 0){
                stat = con.createStatement();
                stat.executeUpdate("UPDATE " + DATABASE_NAME + ".product SET name = '" + product.getTitle() +
                        "', description = '" + product.getDescription() + "', visible = " +
                        product.isVisible() + " WHERE id = " + product.getId() + ";");

                stat.executeUpdate("COMMIT;");
            }
            else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(resultSet, stat, con);
        }
        return true;
    }

    public void writeProductPriorities(List<Product> productList) {
        Connection connection = null;
        Statement statement = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM " + DATABASE_NAME + ".product;");
            statement.executeUpdate("COMMIT;");
            close(null, statement, connection);
            for(Product product : productList) {
                insertProduct(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(null, statement, connection);
        }
    }
}
