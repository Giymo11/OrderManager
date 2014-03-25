package beans;

import dao.ProductDAO;
import dto.Product;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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
    private ProductDAO productDAO;

    private String newText;
    private String newName;
    private float newPrice;
    private List<Product> selectedCatProducts;

    private String selectedCategory;
    private String selectedPicture;

    public ProductBean() {
        productDAO = new ProductDAO();
        selectedCategory = null;
        selectedCatProducts = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return productDAO.getProductList();
    }

    public void delete() {
        int id = Integer.parseInt(fetchParameter("id"));
        productDAO.delete(id);
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }

    public void addNewProduct() {
        productDAO.addNewProduct(selectedCategory, newName, newText, newPrice, selectedPicture);

        newName = "";
        newText = "";
        newPrice = 0.0f;
    }

    public void save(){
        int id = Integer.parseInt(fetchParameter("idS"));
        productDAO.save(id);
    }

    public void getProductsByCategory() {
        selectedCatProducts = productDAO.getProductsByCategory(selectedCategory);
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
        if(selectedCatProducts.isEmpty())
            getProductsByCategory();
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

    public String getName(int id){
        for(Product p : productDAO.getProductList())
            if(p.getId() == id)
                return p.getTitle();
        return null;
    }

    public float getPrice(int id){
        for(Product p : productDAO.getProductList())
            if(p.getId() == id)
                return p.getPrice();
        return 0;
    }
}
