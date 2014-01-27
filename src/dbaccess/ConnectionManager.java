package dbaccess;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * The ConnectionManager fetches a connection via JNDI e.g. from Tomcat,
 * which support connection pooling and prepared statement pooling.
 *
 * @author posch
 */

public class ConnectionManager {
    /**
     * @param dataSource the name of the datasource defined in context.xml or web.xml
     * @param autoCommit determines if autocommit is set
     * @return a connection from tomcat connectionmanager, which support connection pooling.
     *         JDBC 2 does not support Statement pooling, but JDBC 3 does.
     *         Prepared Statements are pooled entirely under the covers, that means that there is no diference between jdbc 2 and 3 code.
     *         This means that under JDBC 3.0, your existing code will automatically leverage statement pooling.
     *         Unfortunately, this also means that you do not have control over which prepared statements are pooled,
     *         only the number of statements that are cached.
     */

    public static Connection getConnection(String dataSource, boolean autoCommit) {
        Context ctx;
        Connection connection = null;

        try {
            ctx = new InitialContext();

            Context envcontext = (Context) ctx.lookup("java:comp/env/");
            DataSource ds = (DataSource) envcontext.lookup(dataSource);

            connection = ds.getConnection();
            // setup the connection
            connection.setAutoCommit(autoCommit);

        } catch (NamingException ex) {
            System.out.println("Naming exception:     " + ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        if (connection == null)
            System.out.println("Connection is null in getConnection");
        return connection;
    }
}
