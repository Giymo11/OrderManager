package dao;

import dto.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Sarah on 31.03.2014.
 */
public class SettingsDAO extends JDBCDAO {
    public SettingsDAO(){
        super();
    }

    public boolean check(String password, String email){
        boolean check = false;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT hash FROM " + DATABASE_NAME + ".user WHERE email = '" + email +"';");

            if(resultSet.next()){
                check = checkPassword(resultSet.getString(1), password, email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
        }
        return check;
    }

    private boolean checkPassword(String hash, String password, String email) {
        if(hash(password.concat(email)).equals(hash))
            return true;
        return false;
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
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public int writeEmailAndPass(String oldEmail, String newEmail, String newPass1, String oldPass) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();

            if(newEmail.equals(oldEmail) && !newPass1.equals("")){
                statement.executeUpdate("UPDATE " + DATABASE_NAME + ".user SET hash = '" +
                        hash(newPass1.concat(oldEmail)) + "' WHERE email = '" + oldEmail + "';");
            }
            if(!newEmail.equals(oldEmail)){
                resultSet = statement.executeQuery("SELECT count(*) FROM " + DATABASE_NAME + ".user WHERE email = '"
                        + newEmail + "';");
                resultSet.next();

                if(resultSet.getInt(1)>0)
                    return -1;

                if(newPass1.equals(""))
                    statement.executeUpdate("UPDATE " + DATABASE_NAME + ".user SET email = '" + newEmail + "', hash = '"
                            + hash(oldPass.concat(newEmail)) + "' WHERE email = '" + oldEmail + "';");
                else
                    statement.executeUpdate("UPDATE " + DATABASE_NAME + ".user SET email = '" + newEmail + "', hash = '"
                            + hash(newPass1.concat(newEmail)) + "' WHERE email = '" + oldEmail + "';");
            }
            statement.executeUpdate("COMMIT;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
        }
        return 1;
    }

    public User getUser(String email) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT * FROM " + DATABASE_NAME + ".user WHERE email = '" + email + "';");

            if(resultSet.next())
                user = getUserWithResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private User getUserWithResultSet(ResultSet resultSet) throws SQLException {
        User user = new User(resultSet.getString("Email"),
                                resultSet.getString("FirstName"),
                                resultSet.getString("LastName"),
                                resultSet.getString("hash"),
                                resultSet.getString("telnr"),
                                resultSet.getInt("addressid"));
        user.setId(resultSet.getInt("id"));
        return user;
    }
}
