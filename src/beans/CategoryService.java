package beans;

import dao.CategoryDao;
import dto.Category;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 20.01.14
 * Time: 14:47
 * The Service class for Categories (General Data)
 */

@ManagedBean
@ApplicationScoped
public class CategoryService {
    private String newName;
    private List<String> names;
    private CategoryDao categoryDao;
    private List<Category> categories;

    public CategoryService() {
        categoryDao = new CategoryDao();
    }

    public List<Category> getCategories() {
        if (categories == null) {
            categories = categoryDao.getCategories();
        }
        return categories;
    }

    private Category getCategoryForId(int id){
        for(Category cat : categories)
            if(cat.getId() == id)
                return cat;
        return null;
    }

    public String delete() {
        int id = Integer.parseInt(fetchParameter("id"));
        Category cat = getCategoryForId(id);
        names.remove(cat.getName());
        for(int i = 0; i<categories.size(); i++)
            if(categories.get(i).getId() == cat.getId())
                categories.remove(i);
        categoryDao.delete(id);
        return "#";
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }

    public String addNewCategory() {
        if(categoryDao.addCategory(new Category(newName))) {
            categories.add(new Category(newName));
            names.add(newName);
        }
        newName = "";
        return "#";
    }

    public String save() {
        int id = Integer.parseInt(fetchParameter("idS"));
        categoryDao.save(getCategoryForId(id));
        categories = categoryDao.getCategories();
        names = null;
        names = getNames();
        return "#";
    }

    public List<String> getNames() {
        if (names == null) {
            names = new ArrayList();
            for(Category cat : categoryDao.getCategories())
                names.add(cat.getName());
        }
        return names;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getNameForID(int id){
        for(Category cat : getCategories())
            if(cat.getId() == id) {
                cat.getName();
                return cat.getName();
            }
        return "asdf";
    }
}
