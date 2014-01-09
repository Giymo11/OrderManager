package dbaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {
    private final static Logger log = Logger.getLogger(ConnectionManager.class.getName());
    private static ConnectionManager connectionManager = null;
    private Connection connection = null;

    private String username = "root";
    private String password = "passwort";
    private String url = "jdbc:mysql://localhost:3306/mysql";

    private ConnectionManager() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
        connection = DriverManager.getConnection(url, username, password);
    }

    public static ConnectionManager getInstance() throws SQLException {
        if (connectionManager == null) {
            connectionManager = new ConnectionManager();
        }
        return connectionManager;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
