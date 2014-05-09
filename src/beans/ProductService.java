package beans;

import dao.ProductDao;
import dto.Product;

import javax.faces.application.FacesMessage;
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
 * Time: 13:30
 */

@ManagedBean
@ApplicationScoped
public class ProductService {
    private ProductDao productDao;

    private String newText;
    private String newName;
    private float newPrice;
    private List<Product> selectedCatProducts;
    private List<String> productNames;
    private List<Product> productList;
    private List<String> orderProducts;

    private String selectedCategory;
    private String selectedPicture;

    public ProductService() {
        productDao = new ProductDao();
        selectedCategory = null;
        selectedCatProducts = new ArrayList();
        newName = "";
        newText = "";
        newPrice = 0.0f;
    }

    public List<Product> getProducts() {
        if (productList == null) {
            productList = productDao.getProductList();
        }
        return productList;
    }

    public String delete() {
        int id = Integer.parseInt(fetchParameter("id"));
        productDao.delete(id);

        for(int i=0; i<productList.size(); i++)
            if(productList.get(i).getId() == id)
                productList.remove(i);
        return "#";
    }

    public String fetchParameter(String param) {
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String value = (String) parameters.get(param);

        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Could not find parameter '" + param + "' in request parameters");

        return value;
    }

    public String addNewProduct() {
        if(newPrice==0.0f || selectedCategory==null || newName.equals("") || newText.equals(""))
            FacesContext.getCurrentInstance().addMessage("Failure", new FacesMessage("Bitte überprüfen Sie Ihre Eingaben!", "Preis muss größer 0 sein; Name, Beschreibung und Kategorie müssen vorhanden sein"));
        else {
            Product p = productDao.addNewProduct(selectedCategory, newName, newText, newPrice, selectedPicture);
            if(p!=null) {
                productList.add(p);
                productNames.add(newName);
                orderProducts = null;
                getOrderProducts();
            }
            else
                FacesContext.getCurrentInstance().addMessage("Failure", new FacesMessage("Achtung, zwei gleiche Namen sind nicht erlaubt!"));

            newName = "";
            newText = "";
            newPrice = 0.0f;
        }
        return "#";
    }

    public String save(){
        int id = Integer.parseInt(fetchParameter("idS"));
        for(Product product : productList)
            if(product.getId() == id)
                if(!productDao.save(product)) {
                    FacesContext.getCurrentInstance().addMessage("Failure", new FacesMessage("Achtung, zwei gleiche Namen sind nicht erlaubt!"));
                    productList = productDao.getProductList();
                    productNames = null;
                    getProductNames();
                }
        return "#";
    }

    public void getProductsByCategory() {
        selectedCatProducts = productDao.getProductsForCategory(selectedCategory);
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

    public String getNameForID(int id){
        if(productList == null)
            productList = productDao.getProductList();
        for(Product p : productList) {
            if (p.getId() == id) {
                return p.getTitle();
            }
        }
        return null;
    }

    public float getPrice(int id){
        if(productList == null)
            productList = productDao.getProductList();

        for(Product p : productList)
            if(p.getId() == id)
                return p.getPrice();
        return 0;
    }

    public  List<String> getProductNames(){
        if(productList == null)
            productList = productDao.getProductList();

        if(productNames==null){
            productNames = new ArrayList();
            for(Product p : productList){
                productNames.add(p.getTitle());
            }
        }
        return productNames;
    }

    public String getFormatedPrice(float price){
        String temp = price+"";
        String formated;
        String str = temp.substring(temp.indexOf('.'));

        if (str.length()==2)
            formated = "€ " + temp + "0";
        else if (str.length()>3)
            formated = "€ " + temp.substring(0, temp.indexOf('.')) + str.substring(0,3);
        else
            formated = "€ " + temp;

        formated = formated.replace('.', ',');

        return formated;
    }

    public List<String> getOrderProducts(){
        if(orderProducts == null) {
            orderProducts = new ArrayList();
            for (Product product : productList)
                orderProducts.add(product.getTitle());
        }

        return orderProducts;
    }

    public void setOrderProducts(List<String> list){
        orderProducts = list;
    }

    public void saveOrder(){
        int priority = orderProducts.size()*10;
        for(String str : orderProducts)
            for(Product product: productList)
                if(product.getTitle().equals(str)) {
                    product.setPriority(priority);
                    priority-=10;
                }
        productDao.writeProductPriorities(productList);
        productList = productDao.getProductList();
    }
}
