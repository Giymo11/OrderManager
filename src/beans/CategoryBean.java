package beans;

import dao.CategoryDAO;
import dto.Category;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
    private String newName;
    private List<String> names;
    private CategoryDAO categoryDAO;

    public CategoryBean() {
        categoryDAO = new CategoryDAO();
    }

    public List<Category> getCategories() {
        return categoryDAO.getCategories();
    }

    public void delete() {
        int id = Integer.parseInt(fetchParameter("id"));
        categoryDAO.delete(id);
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }

    public void addNewCategory() {
        categoryDAO.addCategory(new Category(newName));
        newName = "";
    }

    public void save() {
        int id = Integer.parseInt(fetchParameter("idS"));
        categoryDAO.save(id);
    }

    public List<String> getNames() {
        names = new ArrayList<>();

        for (Category category : categoryDAO.getCategories())
            names.add(category.getName());

        return names;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
