package beans;

import dbaccess.ConnectionManager;
import dto.Category;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
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
 * Time: 14:47
 * To change this template use File | Settings | File Templates.
 */

@ManagedBean
public class CategoryBean {
    private List<Category> categories;

    private String newName;
    private List<String> names;

    private int lastID = 0;
    private ConnectionManager connectionManager;
    private Connection connection;

    public CategoryBean() {
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("jdbc/dataSource", false);
        categories = new ArrayList<>();
        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() throws IOException {
        try {
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM ordermanager.category");
            Category cat = null;
            while (res.next()) {
                cat = getCategoryWithResultSet(res);

                if (cat != null)
                    categories.add(cat);

                if (lastID < res.getInt(1)) {
                    lastID = res.getInt(1);
                }
            }

            statement.close();
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Category getCategoryWithResultSet(ResultSet res) throws SQLException {
        return new Category(res.getInt(1), res.getString(2));
    }

    public void insertCategory(Category category) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO ordermanager.Category VALUES(" + category.getSQLString() + ");");

            categories.add(category);

        } catch (SQLException e) {
            e.printStackTrace();
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

    public void delete() {
        int id = Integer.getInteger(fetchParameter("id"));
        ResultSet res;

        try {
            Statement statement = connection.createStatement();
            res = statement.executeQuery("SELECT * FROM ordermanager.Category WHERE ID = " + id + ";");

            Category category = getCategoryWithResultSet(res);
            categories.remove(category);

            connection.createStatement().executeUpdate("DELETE FROM ordermanager.Category WHERE ID = " + id + ";");
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

    public void addNewCategory() {
        for (Category cat : categories) {
            if (cat.getName().toUpperCase().equals(newName.toUpperCase())) {
                FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("Fehler! Diese Kategorie existiert bereits"));
                return;
            }
        }
        insertCategory(new Category(++lastID, newName));
    }

    public List<String> getNames() {
        names = new ArrayList<>();

        if (categories == null)
            try {
                read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        for (Category category : categories)
            names.add(category.getName());

        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
