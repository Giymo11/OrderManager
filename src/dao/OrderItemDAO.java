package dao;

import dto.OrderItem;

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
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */
public class OrderItemDao extends JdbcDao {
    public OrderItemDao(){
        super();
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

    public void addOrderItem(OrderItem orderItem){
        Connection connection = null;
        Statement statement = null;

        int id = getExistingID(orderItem);
        try{
            if(id==-1){
                insertObject("orderitem", orderItem);

                connection = getConnection();
                statement = connection.createStatement();
                statement.executeUpdate("UPDATE " + DATABASE_NAME + ".orderitem SET orderid = " + orderItem.getOrderid() +
                        ", productid = " + orderItem.getProductid() + ", ordered = " + orderItem.getOrdered() +
                        ", delivered = " + orderItem.getDelivered() + " WHERE id = " + orderItem.getId() + ";");
                statement.executeUpdate("COMMIT;");
            }
            else{
                connection = getConnection();
                statement = connection.createStatement();
                statement.executeUpdate("UPDATE " + DATABASE_NAME + ".orderitem SET ordered = ordered + " + orderItem.getOrdered() +
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
            resultSet = statement.executeQuery("SELECT count(*), id FROM " + DATABASE_NAME + ".orderitem WHERE orderid = "
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

    public List<OrderItem> getAllOrderItemsForUser(String email) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<OrderItem> orderItems = new ArrayList();

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".orderitem WHERE orderid IN " +
                    "(SELECT orderid from " + DATABASE_NAME +".order WHERE addressid = " +
                    "(SELECT addressid FROM " + DATABASE_NAME + ".user " +
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
        List<OrderItem> orderItems = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".orderitem WHERE orderid = " +
                    "(SELECT id FROM " + DATABASE_NAME + ".order WHERE tourid = " +
                    "(SELECT id FROM " + DATABASE_NAME + ".tour WHERE date = '" + getDateSQL(date) + "')" +
                        "AND addressid = (SELECT addressid FROM " + DATABASE_NAME + ".user " +
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

    public List<OrderItem> getAllItemsForDate(Date startDate) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        List<OrderItem> items = new ArrayList();

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".orderitem WHERE orderid IN " +
                "(SELECT id FROM " + DATABASE_NAME + ".order WHERE tourid IN (SELECT id FROM " + DATABASE_NAME + ".tour" +
                    " WHERE date = '" + getDateSQL(startDate) + "'));");
            int count=0;
            while(resultSet.next()){
                OrderItem item = getOrderItemWithResultSet(resultSet);
                for(OrderItem i : items)
                    if(i.getProductid() == item.getProductid()) {
                        i.setOrdered(i.getOrdered() + item.getOrdered());
                        i.setDelivered(i.getDelivered() + item.getDelivered());
                        ++count;
                    }
                if(count==0) {
                    items.add(item);
                }
                else
                    count = 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
        }
        return items;
    }


    public List<OrderItem> getAllItemsForOrder(int orderID) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        List<OrderItem> items = new ArrayList();

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".orderitem WHERE orderid = "+orderID+";");

            while(resultSet.next()){
                items.add(getOrderItemWithResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
        }
        return items;
    }

    public void update(OrderItem item){
        Connection connection = null;
        Statement statement = null;

        try{
            connection=getConnection();
            statement=connection.createStatement();
            statement.executeUpdate("UPDATE " + DATABASE_NAME + ".orderitem set delivered = " + item.getDelivered()+";");
            statement.executeUpdate("COMMIT;");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        finally{
            close(null, statement, connection);
        }
    }

    public void insertItem(OrderItem item, int addressID){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT id FROM " + DATABASE_NAME + ".order WHERE addressID = "+addressID+" AND tourid = " +
                    "(SELECT id FROM "+DATABASE_NAME+".tour WHERE date = '"+getDateSQL(new Date())+"');");
            resultSet.next();
            item.setOrderid(resultSet.getInt(1));
            addOrderItem(item);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close(resultSet,statement,connection);
        }
    }
}
