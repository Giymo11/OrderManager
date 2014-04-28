package dao;

import dto.Category;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 24.02.14
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
public class CategoryDao extends JdbcDao {
    public CategoryDao(){
        super();
    }

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList();

        Connection connection = null;
        Statement statement = null;
        ResultSet res = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".category");
            Category cat;

            while (res.next()) {
                cat = getCategoryWithResultSet(res);

                if (cat != null)
                    categories.add(cat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(res, statement, connection);
        }
        return categories;
    }

    private Category getCategoryWithResultSet(ResultSet res) throws SQLException {
        return new Category(res.getInt(1), res.getString(2));
    }

    public boolean addCategory(Category cat){
        if(exists(cat)){
            FacesContext.getCurrentInstance().addMessage("Failure", new FacesMessage("Diese Kategorie existiert bereits!"));
            return false;
        }

        insertObject("category", cat);

        Connection connection = null;
        Statement statement = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();

            statement.executeUpdate("UPDATE " + DATABASE_NAME + ".category SET name = '" + cat.getName() + "' WHERE id = " + cat.getId() + ";");
            statement.executeUpdate("COMMIT;");

        } catch (SQLException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("Datenbank-Fehler!"));
        }
        finally{
            close(null, statement, connection);
        }
        return true;
    }

    private boolean exists(Category cat) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT count(*) FROM " + DATABASE_NAME +
                        ".category WHERE name = '" + cat.getName() + "';");
            resultSet.next();

            if(resultSet.getInt(1)>0)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
        }
        return false;
    }

    public void delete(int id){
        ResultSet res = null;
        Statement stat = null;
        Connection con = null;
        try {
            con = getConnection();
            stat = con.createStatement();
            res = stat.executeQuery("SELECT count(*) FROM " + DATABASE_NAME + ".product WHERE categoryid = " + id + ";");
            res.next();
            if (res.getInt(1) > 0) {
                FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("Bitte löschen Sie zuerst alle Produkte aus dieser Kategorie!"));
                return;
            }
            deleteObject("category", id);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            close(res, stat, con);
        }
    }

    public void save(Category cat){
        Connection connection = null;
        Statement stat = null;
        try {
            connection = getConnection();
            stat = connection.createStatement();

            stat.executeUpdate("UPDATE " + DATABASE_NAME + ".category SET name = '" + cat.getName() + "' WHERE id = " + cat.getId() + ";");
            stat.executeUpdate("COMMIT;");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally{
            close(null, stat, connection);
        }
    }
}
