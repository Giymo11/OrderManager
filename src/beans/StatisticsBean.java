package beans;

import dao.StatisticsDAO;
import org.primefaces.model.chart.CartesianChartModel;

import javax.faces.bean.ManagedBean;
import java.util.Date;

/**
 * Created by Sarah on 08.04.2014.
 */
@ManagedBean
public class StatisticsBean {
    private String selectedProduct;
    private String selectedCategory;
    private Date startDate;
    private Date endDate;
    private StatisticsDAO statisticsDAO;

    private CartesianChartModel modelCategory;
    private CartesianChartModel modelProduct;

    public StatisticsBean(){
        statisticsDAO = new StatisticsDAO();
        setSelectedProduct("");
        setSelectedCategory("Brote");
        setStartDate(new Date());
        startDate.setDate(01);
        setEndDate(new Date());
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
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void updateCat(){
        modelCategory = statisticsDAO.getModelForCategory(selectedCategory, startDate, endDate);
    }

    public void updateProd(){
        modelProduct = statisticsDAO.getModelForProduct(selectedProduct, startDate, endDate);
    }
}
