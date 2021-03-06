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
public class OrderDao extends JdbcDao {
    public OrderDao(){
        super();
    }

    public List<Order> getOrderList(){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Order order;
        List<Order> orderList = new ArrayList();

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".order;");

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

        return orderList;
    }

    private Order getOrderWithResultSet(ResultSet resultSet) throws SQLException {
        Order order = new  Order(resultSet.getInt("tourid"),
                resultSet.getInt("addressid"),
                resultSet.getString("memoForCustomer"),
                resultSet.getString("memoForPock"),
                resultSet.getBoolean("delivered"));
        order.setId(resultSet.getInt("id"));
        return order;
    }

    public Order addOrder(int tourID, String email, String memo){
        int addressid = getAddressIDForEmail(email);
        if(memo==null)
            memo="";
        Order order =  new Order(tourID, addressid, "", memo, false);

        Connection connection = null;
        Statement statement = null;

        int id = getExistingOrder(tourID, addressid);

        try{
            if(id == -1){
                insertObject("Order", order);

                connection = getConnection();
                statement = connection.createStatement();

                statement.executeUpdate("UPDATE " + DATABASE_NAME + ".order SET tourid = " + order.getTourid() + ", addressid = "
                        + order.getAddressid() + ", memoForCustomer = '" + order.getMemoForCustomer() + "', memoForPock = '"
                        + order.getMemoForPock() + "' WHERE id = " + order.getId());
                statement.executeUpdate("COMMIT;");
            }
            else{
                FacesContext.getCurrentInstance().addMessage("Achtung", new FacesMessage("FÃ¼r diesen Tag gibt es schon eine Bestellung"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(null, statement, connection);
        }

        return order;
    }

    private int getExistingOrder(int tourID, int addressid) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT id from " + DATABASE_NAME + ".order WHERE tourid = " + tourID +
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

    public int getAddressIDForEmail(String email) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        int id = -1;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT addressid from " + DATABASE_NAME + ".user WHERE email = '" + email + "';");

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

    public List<Order> getOrdersInDateRange(Date start, Date end, String email) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Order> orders = new ArrayList();
        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".order AS orderT JOIN" +
                    "(SELECT id `TourID`, date FROM " + DATABASE_NAME + ".tour WHERE date BETWEEN " +
                            "'" + getDateSQL(start) + "' AND '" + getDateSQL(end) + "') AS tour " +
                    "ON orderT.tourid = tour.TourID " +
                    "AND addressid = (SELECT addressid FROM " + DATABASE_NAME + ".user " +
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

    public List<Order> getOrdersByDate(Date date){
        Connection connection = null;
        Statement statement = null;
        Statement statement2 = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        List<Order> orders = new ArrayList();

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".address JOIN (SELECT * FROM "
                    + DATABASE_NAME + ".town) AS town JOIN " +
                    "(SELECT * FROM " + DATABASE_NAME + ".order) AS oneorder " +
                    "ON address.townid = town.id AND address.id = oneorder.addressid AND oneorder.tourid = " +
                    "(SELECT id FROM " + DATABASE_NAME + ".tour WHERE date = '" + getDateSQL(date) + "') " +
                    " AND oneorder.memoForPock <> '' ORDER BY town.name desc;");

            while(resultSet.next()){
                orders.add(getOrderWithResultSet(resultSet));
            }

            statement2 = connection.createStatement();
            resultSet2 = statement2.executeQuery("SELECT * FROM " + DATABASE_NAME + ".address JOIN (SELECT * FROM "
                    + DATABASE_NAME + ".town) AS town JOIN " +
                    "(SELECT * FROM " + DATABASE_NAME + ".order) AS oneorder " +
                    "ON address.townid = town.id AND address.id = oneorder.addressid AND oneorder.tourid = " +
                    "(SELECT id FROM " + DATABASE_NAME + ".tour WHERE date = '" + getDateSQL(date) + "') " +
                    "AND oneorder.memoForPock = '' ORDER BY town.name desc;");

            while(resultSet2.next())
                orders.add(getOrderWithResultSet(resultSet2));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
            close(resultSet2, statement2, null);
        }
        return orders;
    }

    public void writeMemoWithID(int id, String memo, String forWhom) {
        Connection connection = null;
        Statement statement = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();

            statement.executeUpdate("UPDATE " + DATABASE_NAME + ".order SET memoFor" + forWhom + " = '" + memo + "' WHERE id = " + id + ";");
            statement.executeUpdate("COMMIT;");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(null, statement, connection);
        }
    }

    public List<Order> getOrdersByAddress(int addressID){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Order order;
        List<Order> orderList = new ArrayList();

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".order where AddressID = " +addressID+ ";");

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

        return orderList;
    }

    public Order getOrderByAddressForCurrentDay(int addressID) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Order order = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".order where AddressID = "
                    + addressID + " AND tourid = (SELECT id FROM " + DATABASE_NAME + ".tour WHERE date = '" + getDateSQL(new Date()) + "');");

            while(resultSet.next()){
                order = getOrderWithResultSet(resultSet);
                order.setId(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
        }

        return order;
    }

    public int addOrderWithAddressID(int addressID, Date date){
        int id = 0;
        Connection connection = null;
        Statement statement = null;
        Statement statement1 = null;
        ResultSet resultSet = null;
        ResultSet resultSet1 = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT id from " + DATABASE_NAME + ".tour WHERE date = '" + getDateSQL(date) + "';");

            if (!resultSet.next()) {
                close(resultSet, statement, connection);
                new TourDao().addTour(new Date());
            }
            connection = getConnection();
            statement1 = connection.createStatement();
            resultSet1 = statement1.executeQuery("SELECT id from " + DATABASE_NAME + ".tour WHERE date = '" + getDateSQL(date) + "';");

            if(resultSet1.next()) {
                Order order = new Order(resultSet1.getInt(1), addressID, "", "", true);
                close(resultSet1, statement1, connection);
                insertObject("order", order);
                id = order.getId();

                String sqlstring = "UPDATE " + DATABASE_NAME + ".order SET addressID = " + order.getAddressid() +
                        ", tourID = " + order.getTourid() + ", delivered = 1, memoForPock = '', memoForCustomer = '' WHERE id = " + order.getId() + ";";

                connection = getConnection();
                statement = connection.createStatement();
                statement.executeUpdate(sqlstring);
                statement.executeUpdate("COMMIT;");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            close(resultSet,statement,connection);
            close(resultSet1, statement1, null);
        }
        return id;

    }


    public void writeMemoWithAddressID(int addressID, Date date, String memo){
        int orderID;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            String sqlstring = "SELECT id FROM " + DATABASE_NAME + ".order WHERE addressid = " + addressID + " AND tourID = (SELECT id from " + DATABASE_NAME + ".tour WHERE date = \'" + getDateSQL(date) + "\');";

            resultSet = statement.executeQuery(sqlstring);
            if (!resultSet.next()) {
                orderID = addOrderWithAddressID(addressID, new Date());
            } else
                orderID = resultSet.getInt(1);

            writeMemoWithID(orderID, memo, "Customer");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            close(resultSet, statement, connection);
        }
    }

    public void writeDeliveredStatus(Order order) {
        Connection connection = null;
        Statement statement = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();

            statement.executeUpdate("UPDATE " + DATABASE_NAME + ".order SET delivered = " + order.isDelivered() +
                    " WHERE id = " + order.getId() + ";");
            statement.executeUpdate("COMMIT;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            close(null, statement, connection);
        }
    }
}
