package beans;

import dao.StatisticsDao;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.CartesianChartModel;

import javax.faces.bean.ManagedBean;
import java.util.Date;

/**
 * Created by Sarah on 08.04.2014.
 */
@ManagedBean
public class StatisticsService {
    private String selectedProduct;
    private String selectedCategory;
    private Date startDate;
    private Date endDate;
    private StatisticsDao statisticsDAO;

    private CartesianChartModel modelCategory;
    private CartesianChartModel modelProduct;

    public StatisticsService(){
        statisticsDAO = new StatisticsDao();
        setSelectedProduct("Brot");
        setSelectedCategory("Brote");
        setStartDate(new Date());
        startDate.setDate(1);
        setEndDate(new Date());
        updateProd();
        updateCat();
    }

    public CartesianChartModel getModelForCategory(){
        if (modelCategory == null) {
            updateCat();
        }
        return modelCategory;
    }

    public CartesianChartModel getModelForProduct(){
        if (modelProduct == null) {
            updateProd();

        }
        return modelProduct;
    }

    public String getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(String selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        System.out.println("SetStartDate called");
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        System.out.println("SetEndDate called");
        this.endDate = endDate;
    }

    private void updateCat(){
        modelCategory = statisticsDAO.getModelForCategory(selectedCategory, startDate, endDate);
    }

    private void updateProd(){
        modelProduct = statisticsDAO.getModelForProduct(selectedProduct, startDate, endDate);
    }

    public void handleDateSelectStart(SelectEvent event){
        startDate = (Date) event.getObject();
        updateCat();
        updateProd();
    }

    public void handleDateSelectEnd(SelectEvent event){
        endDate = (Date) event.getObject();
        updateCat();
        updateProd();
    }

    public void handleProductChange(String string){
        updateProd();
    }

    public void handleCategoryChange(String category){
        updateCat();
    }
}
