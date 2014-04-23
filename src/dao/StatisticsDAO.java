package dao;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * Created by Sarah on 08.04.2014.
 */
public class StatisticsDAO extends JdbcDao {
    public StatisticsDAO(){
        super();
    }

    public CartesianChartModel getModelForCategory(String selectedCategory, Date startDate, Date endDate) {
        CartesianChartModel model = new CartesianChartModel();
        ChartSeries category = new ChartSeries();

        Connection connection = null;
        Statement statement = null;
        Statement statName = null;
        ResultSet resultSet = null;
        ResultSet resName = null;
        category.setLabel(selectedCategory);

        try{
            connection = getConnection();
            statement = connection.createStatement();
            statName = connection.createStatement();

            resultSet = statement.executeQuery("SELECT productid, sum(ordered) FROM " + DATABASE_NAME +".orderitem" +
                    " WHERE orderid IN ( SELECT id FROM " + DATABASE_NAME +".order WHERE tourid IN" +
                    " (SELECT id FROM " + DATABASE_NAME + ".tour WHERE date BETWEEN '" + getDateSQL(startDate) +
                    "' AND '" + getDateSQL(endDate) + "') )" +
                    " AND productid IN ( SELECT id FROM  " + DATABASE_NAME + ".product" +
                    " WHERE categoryid = (SELECT id FROM " + DATABASE_NAME + ".category WHERE name = '"
                    + selectedCategory + "') ) GROUP BY productid;");

            while(resultSet.next()){
                resName = statName.executeQuery("SELECT name FROM " + DATABASE_NAME + ".product WHERE id = "
                        + resultSet.getInt(1) + ";");
                resName.next();

                category.set(resName.getString(1), resultSet.getInt(2));
            }
            model.addSeries(category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
            close(resName, statName, null);
        }
        return model;
    }

    private String getDateSQL(Date date) {
        if(date.getMonth()<10 && date.getDate()<10)
            return (date.getYear()+1900) + "-0" + (1+date.getMonth()) + "-0" + date.getDate();
        else
        if(date.getMonth()<10)
            return (date.getYear()+1900) + "-0" + (1+date.getMonth()) + "-" + date.getDate();
        if(date.getDate()<10)
            return (date.getYear()+1900) + "-" + (1+date.getMonth()) + "-0" + date.getDate();


        return (date.getYear()+1900) + "-" + (1+date.getMonth()) + "-" + date.getDate();
    }

    public CartesianChartModel getModelForProduct(String selectedProduct, Date startDate, Date endDate) {
        CartesianChartModel model = new CartesianChartModel();
        ChartSeries product = new ChartSeries();
        product.setLabel(selectedProduct);

        Connection connection = null;
        Statement statement = null;
        Statement statDate = null;
        ResultSet resultSet = null;
        ResultSet resDate = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            statDate = connection.createStatement();

            resultSet = statement.executeQuery("SELECT productid, sum(ordered), orderid FROM " +
                    DATABASE_NAME + ".orderitem WHERE orderid IN " +
                    "( SELECT id FROM " + DATABASE_NAME + ".order WHERE tourid IN " +
                    "(SELECT id FROM " + DATABASE_NAME + ".tour WHERE date BETWEEN '" + getDateSQL(startDate)
                    + "' AND '" + getDateSQL(endDate) + "') ) " + "AND productid = (SELECT id FROM "
                    + DATABASE_NAME + ".product WHERE name = '" + selectedProduct + "') " +
                    "GROUP BY orderid;");

            while(resultSet.next()){
                resDate = statDate.executeQuery("SELECT date FROM " + DATABASE_NAME + ".tour WHERE id = " +
                    "(SELECT tourid FROM " + DATABASE_NAME + ".order WHERE id = " + resultSet.getInt(3) +
                    ");");
                resDate.next();

                product.set(resDate.getString(1), resultSet.getInt(2));

            }
            model.addSeries(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            close(resultSet, statement, connection);
            close(resDate, statDate, connection);
        }

        return model;
    }
}
