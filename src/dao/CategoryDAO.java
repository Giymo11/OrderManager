package dao;

import dto.Category;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;
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
public class CategoryDAO extends JDBCDAO {
    private List<Category> categories;

    public CategoryDAO(){
        categories = new ArrayList<>();
        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void read() throws IOException {
        Connection connection = null;
        Statement statement = null;
        ResultSet res = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            res = statement.executeQuery("SELECT * FROM ordermanager.category");
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
    }

    public List<Category> getCategories() {
        if (categories == null)
            try {
                read();
            } catch (IOException e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("Failed to get offers from database"));
            }
        return categories;
    }

    private Category getCategoryWithResultSet(ResultSet res) throws SQLException {
        return new Category(res.getInt(1), res.getString(2));
    }

    public void addCategory(Category cat){
        for(Category category : categories)
            if(category.getName().equals(cat.getName())){
                FacesContext.getCurrentInstance().addMessage("Failure", new FacesMessage("Achtung! Diese Kategorie existiert bereits!"));
            }

        insertObject("category", cat);

        Connection connection = null;
        Statement statement = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();

            statement.executeUpdate("UPDATE ordermanager.category SET name = '" + cat.getName() + "' WHERE id = " + cat.getId() + ";");
            statement.executeUpdate("COMMIT;");

            categories.add(cat);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally{
            close(null, statement, connection);
        }
    }

    public void delete(int id){
        ResultSet res = null;
        Statement stat = null;
        Connection con = null;
        try {
            con = getConnection();
            stat = con.createStatement();
            res = stat.executeQuery("SELECT count(*) FROM ordermanager.product WHERE categoryid = " + id + ";");
            res.next();
            if (res.getInt(1) > 0) {
                FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("Bitte löschen Sie zuerst alle Produkte aus dieser Kategorie!"));
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            for(int i=0; i<categories.size(); i++){
                if(categories.get(i).getId() == id)
                    categories.remove(i);
            }

            deleteObject("category", id);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(res, stat, con);
        }
    }

    public void save(int id){
        Connection connection = null;
        Statement stat = null;
        try {
            connection = getConnection();
            stat = connection.createStatement();
            for (Category cat : categories) {
                if (cat.getId() == id) {
                    stat.executeUpdate("UPDATE ordermanager.category SET name = '" + cat.getName() + "' WHERE id = " + id + ";");
                    stat.executeUpdate("COMMIT;");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally{
            close(null, stat, connection);
        }
    }
}
