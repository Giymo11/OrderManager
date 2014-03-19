package dao;

import dto.OrderItem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 26.02.14
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */
public class OrderItemDAO extends JDBCDAO {
    private List<OrderItem> orderItemList;

    public OrderItemDAO(){
        super();
        orderItemList = new ArrayList<>();
        read();
    }

    private void read(){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        OrderItem order;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ordermanager.orderItem WHERE delivered=-1");

            while(resultSet.next()){
                order = getOrderItemWithResultSet(resultSet);
                if(order != null)
                    orderItemList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
        }
    }

    private OrderItem getOrderItemWithResultSet(ResultSet res) throws SQLException {
        OrderItem order = new OrderItem(
                res.getInt("orderid"),
                res.getInt("productid"),
                res.getInt("ordered"),
                res.getInt("delivered")
        );
        order.setId(res.getInt("id"));
        return order;
    }

    public List<OrderItem> getDeliveredOrders(){
        List<OrderItem> temp = new ArrayList<>();

        for(OrderItem order : orderItemList)
            if(order.getDelivered() != -1)
                temp.add(order);

        return temp;
    }

    public List<OrderItem> getUndeliveredOrders(){
        List<OrderItem> temp = new ArrayList<>();

        for(OrderItem order : orderItemList)
            if(order.getDelivered() == -1)
                temp.add(order);

        return temp;
    }

    public void addOrderItem(OrderItem orderItem){
        Connection connection = null;
        Statement statement = null;

        int id = getExistingID(orderItem);

        try{
            if(id==-1){
                insertObject("orderitem", orderItem);
                orderItemList.add(orderItem);

                connection = getConnection();
                statement = connection.createStatement();
                statement.executeUpdate("UPDATE ordermanager.orderitem SET orderid = " + orderItem.getOrderid() +
                        ", productid = " + orderItem.getProductid() + ", ordered = " + orderItem.getOrdered() +
                        ", delivered = " + orderItem.getDelivered() + " WHERE id = " + orderItem.getId() + ";");
                statement.executeUpdate("COMMIT;");
            }
            else{
                connection = getConnection();
                statement = connection.createStatement();
                statement.executeUpdate("UPDATE ordermanager.orderitem SET ordered = ordered +" + orderItem.getOrdered() +
                        " WHERE id = " + id + ";");
                statement.executeUpdate("COMMIT;");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(null, statement, connection);
        }
    }

    private int getExistingID(OrderItem orderItem) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT count(*), id FROM ordermanager.orderitem WHERE orderid = "
                    + orderItem.getOrderid() + " AND productid = " + orderItem.getProductid() + ";");

            resultSet.next();
            if(resultSet.getInt(1)==1)
                return resultSet.getInt(2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
        }

        return -1;
    }

    public List<OrderItem> getOrderForUser(String email) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<OrderItem> orderItems = new ArrayList<>();

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ordermanager.orderitem WHERE orderid IN " +
                    "(SELECT orderid from ordermanager.order WHERE addressid = " +
                    "(SELECT addressid FROM ordermanager.user " +
                    "WHERE email = '" + email + "')); ");

            while(resultSet.next()){
                orderItems.add(getOrderItemWithResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            close(resultSet, statement, connection);
        }

        return orderItems;
    }

    public List<OrderItem> getOrderItemsForDate(Date date, String email){
        List<OrderItem> orderItems = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT * FROM ordermanager.orderitem WHERE orderid = " +
                    "(SELECT id FROM ordermanager.order WHERE tourid = " +
                    "(SELECT id FROM ordermanager.tour WHERE date = '" + getDateSQL(date) + "')" +
                        "AND addressid = (SELECT addressid FROM ordermanager.user " +
                            "WHERE email = '" + email + "'));");

            while(resultSet.next())
                orderItems.add(getOrderItemWithResultSet(resultSet));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet,statement,connection);
        }
        return orderItems;
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

    public void deleteOneItem(int id) {
        try {
            deleteObject("orderitem", id);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
