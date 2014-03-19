package dao;

import dto.Order;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 26.02.14
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public class OrderDAO extends JDBCDAO {
    private List<Order> orderList;

    public OrderDAO(){
        super();
        orderList = new ArrayList<>();
        read();
    }

    private void read(){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Order order;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ordermanager.order;");

            while(resultSet.next()){
                order = getOrderWithResultSet(resultSet);
                order.setId(resultSet.getInt("id"));

                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
        }
    }

    private Order getOrderWithResultSet(ResultSet resultSet) throws SQLException {
        Order order = new  Order(resultSet.getInt("tourid"),
                resultSet.getInt("addressid"),
                resultSet.getString("memoForCustomer"),
                resultSet.getString("memoForPock"));
        order.setId(resultSet.getInt("id"));
        return order;
    }

    public void addOrder(int tourID, String email, String memo){
        int addressid = getAddressID(email);
        Order order =  new Order(tourID, addressid, "", memo);

        Connection connection = null;
        Statement statement = null;

        int id = getExistingOrder(tourID, addressid);

        try{
            if(id == -1){
                insertObject("Order", order);

                orderList.add(order);

                connection = getConnection();
                statement = connection.createStatement();

                statement.executeUpdate("UPDATE ordermanager.order SET tourid = " + order.getTourid() + ", addressid = "
                        + order.getAddressid() + ", memoForCustomer = '" + order.getMemoForCustomer() + "', memoForPock = '"
                        + order.getMemoForPock() + "' WHERE id = " + order.getId());
                statement.executeUpdate("COMMIT;");
            }
            else{
                FacesContext.getCurrentInstance().addMessage("Achtung", new FacesMessage("Für diesen Tag gibt es schon eine Bestellung"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(null, statement, connection);
        }
    }

    private int getExistingOrder(int tourID, int addressid) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT id from ordermanager.order WHERE tourid = " + tourID +
                        " AND addressid = " + addressid + ";");
            if(resultSet.next())
                return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
        }
        return -1;
    }

    private int getAddressID(String email) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        int id = -1;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT addressid from ordermanager.user WHERE email = '" + email + "';");

            if(resultSet.next())
                id = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
        }

        return id;
    }

    public int getOrderID(int tourID, String email) {
        int addressID = getAddressID(email);

        for(Order order : orderList){
            if(order.getTourid() == tourID && order.getAddressid() == addressID)
                return order.getId();
        }

        return -1;
    }

    public int getTourIDWithID(int orderid) {
        for(Order order : orderList){
            if(order.getId() == orderid)
                return order.getTourid();
        }
        return -1;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public List<Order> getOrdersInDateRange(Date start, Date end, String email) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Order> orders = new ArrayList<>();
        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ordermanager.order AS orderT JOIN" +
                    "(SELECT id `TourID`, date FROM ordermanager.tour WHERE date BETWEEN " +
                            "'" + getDateSQL(start) + "' AND '" + getDateSQL(end) + "') AS tour " +
                    "ON orderT.tourid = tour.TourID " +
                    "AND addressid = (SELECT addressid FROM ordermanager.user " +
                    "WHERE email = '" + email + "') ORDER BY tour.date desc;");

            while(resultSet.next()){
                orders.add(getOrderWithResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
        }
        return orders;
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
}
