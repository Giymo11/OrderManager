package dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Sarah on 27.04.2014.
 */
public class AdminSettingsDao extends JdbcDao {
    public AdminSettingsDao(){
        super();
    }

    public void addAsAdmin(String email, String pass, int id) {
        Connection connection = null;
        Statement statement = null;
        Statement statement1 = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();
            statement1 = connection.createStatement();
            resultSet = statement.executeQuery("SELECT hash FROM " + DATABASE_NAME + ".user WHERE email = '" + email + "';");
            resultSet.next();

            if(resultSet.getString(1).equals(hash(pass.concat(email)))){
                statement1.executeUpdate("INSERT INTO " + DATABASE_NAME + ".admin VALUES(" + id + ");");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            close(resultSet, statement, connection);
            close(null, statement1, null);
        }
    }

    public String hash(String saltedPass) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(saltedPass.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuilder sb = new StringBuilder();
        for (byte aByteData1 : byteData) {
            sb.append(Integer.toString((aByteData1 & 0xff) + 0x100, 16).substring(1));
        }

        //convert the byte to hex format method 2
        StringBuilder hexString = new StringBuilder();
        for (byte aByteData : byteData) {
            String hex = Integer.toHexString(0xff & aByteData);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
