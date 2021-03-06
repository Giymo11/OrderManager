package dao;


import dbaccess.ConnectionManager;
import interfaces.Identifiable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: Sarah
 * Date: 19.02.14
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public class JdbcDao {
    private ConnectionManager connectionManager;
    protected final static String DATABASE_NAME = "pock_tc7";
    private final static String LAST_INSERT_ID = "SELECT max(id) as maxId FROM " + DATABASE_NAME + ".";
    private final static String LAST_INSERT_PIC_ID = "SELECT max(pictureid) as maxId FROM " + DATABASE_NAME + ".";

    public JdbcDao(){
        if(connectionManager==null)
            connectionManager = new ConnectionManager();
    }

    protected Connection getConnection() throws SQLException {
        return connectionManager.getConnection("jdbc/dataSource", false);
    }

    public void insertObject(String tableName, Identifiable identifiable){
        Statement statement = null;
        Connection connection = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO " + DATABASE_NAME + "." + tableName + " VALUES();");
            statement.executeUpdate("COMMIT;");

            int lastId = getLastInsertId(connection, tableName);
            identifiable.setId(lastId);

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            close(null, statement, connection);
        }
    }

    public void deleteObject(String tableName, int id) throws SQLException{
        Statement statement = null;
        final String DEL_STATEMENT;
        if(tableName.equals("picture"))
            DEL_STATEMENT = "DELETE FROM " + DATABASE_NAME + "." + tableName + " WHERE pictureid = " + id + ";";
        else
            DEL_STATEMENT = "DELETE FROM " + DATABASE_NAME + "." + tableName + " WHERE id = " + id + ";";
        Connection con = null;

        try {
            con = getConnection();
            statement = con.createStatement();
            statement.executeUpdate(DEL_STATEMENT);
            statement.executeUpdate("COMMIT;");

        } catch (SQLException se) {
            throw se;
        } finally {
            close(null, statement, con);
        }
    }

    protected int getLastInsertId(Connection con, String tablename){
        int lastId = -1;
        if (con != null) {
            Statement statement = null;
            ResultSet rs = null;
            try {
                String sqlCommand;
                if(tablename.equals("picture"))
                    sqlCommand = LAST_INSERT_PIC_ID + tablename + ";";
                else
                    sqlCommand = LAST_INSERT_ID + tablename + ";";

                statement = con.createStatement();
                rs = statement.executeQuery(sqlCommand);
                if (rs.next()) {
                    lastId = rs.getInt("maxId");
                }
            } catch (SQLException se) {
                se.printStackTrace();
            } finally {
                close(rs, statement, null);
            }
        }
        return lastId;
    }

    public void close(ResultSet rs, Statement statement, Connection connection) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        if (connection != null){
            try {
                connection.close();
            } catch (SQLException ex){
                ex.printStackTrace();
            }
        }
    }
}
