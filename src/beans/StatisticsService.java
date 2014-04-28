package beans;

import dao.StatisticsDao;
import org.primefaces.model.chart.CartesianChartModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.Date;

/**
 * Created by Sarah on 08.04.2014.
 */
@ManagedBean
@SessionScoped
public class StatisticsService {
    private String selectedProduct;
    private String selectedCategory;
    private Date startDate;
    private Date endDate;
    private StatisticsDao statisticsDao;

    private CartesianChartModel modelCategory;
    private CartesianChartModel modelProduct;

    public StatisticsService(){
        statisticsDao = new StatisticsDao();
        setStartDate(new Date());
        startDate.setDate(1);
        setEndDate(new Date());
        endDate.setDate(30);
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

    private void updateCat(){
        if(selectedCategory == null)
            selectedCategory = "";
        modelCategory = statisticsDao.getModelForCategory(selectedCategory, startDate, endDate);
    }

    private void updateProd(){
        if(selectedProduct == null)
            selectedProduct = "";
        modelProduct = statisticsDao.getModelForProduct(selectedProduct, startDate, endDate);
    }

    public void update() {
        updateCat();
        updateProd();
    }
}
