package beans;

import dbaccess.ConnectionManager;
import dto.Category;

import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
@SessionScoped
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
            connection.createStatement().executeUpdate("COMMIT;");

            categories.add(category);
            statement.close();
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
        int id = Integer.parseInt(fetchParameter("id"));

        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT count(*) FROM ordermanager.product WHERE categoryid = " + id + ";");
            res.next();
            if (res.getInt(1) > 0) {
                FacesContext.getCurrentInstance().addMessage("Failure!", new FacesMessage("Bitte löschen Sie zuerst alle Produkte aus dieser Kategorie!"));
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            for (int i = 0; i < categories.size(); ++i) {
                if (categories.get(i).getId() == id)
                    categories.remove(i);
            }

            connection.createStatement().executeUpdate("DELETE FROM ordermanager.Category WHERE ID = " + id + ";");
            connection.createStatement().executeUpdate("COMMIT;");
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
                FacesContext.getCurrentInstance().addMessage("Failure!",
                        new FacesMessage("Fehler! Diese Kategorie existiert bereits"));
                return;
            }
        }
        insertCategory(new Category(++lastID, newName));
    }

    public void save() {
        int id = Integer.parseInt(fetchParameter("idS"));
        try {
            for (Category cat : categories) {
                if (cat.getId() == id) {
                    connection.createStatement().executeUpdate("UPDATE ordermanager.category SET name = '" +
                            cat.getName() + "' WHERE id = " + id + ";");
                    connection.createStatement().executeUpdate("COMMIT;");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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

    @PreDestroy
    public void preDestroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
