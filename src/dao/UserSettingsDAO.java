package dao;

import dto.Address;
import dto.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * Created by Sarah on 31.03.2014.
 */
public class UserSettingsDAO extends JdbcDao {
    public UserSettingsDAO(){
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
        StringBuilder sb = new StringBuilder();
        for (byte aByteData : byteData) {
            sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
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
                resultSet.getDate("birthdate"),
                resultSet.getInt("addressid"),
                resultSet.getBoolean("verified"),
                resultSet.getBoolean("blocked"));
        user.setId(resultSet.getInt("id"));
        return user;
    }


    public String getSelectedTown(User user) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT name FROM " + DATABASE_NAME + ".town WHERE id = (SELECT townID FROM " +
                    DATABASE_NAME + ".address WHERE id = " + user.getAddressID() + ");");

            if(resultSet.next())
                return resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(resultSet, statement, connection);
        }

        return null;
    }

    public boolean saveUserData(User user, Address address, String selectedTown) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();

            statement.executeUpdate("UPDATE " + DATABASE_NAME + ".user SET firstname = '" + user.getFirstName()
                    + "', lastname = '" + user.getLastName() + "', telnr = '" + user.getTelNr() + "', birthdate = '"
                    + getDateSQL(user.getBirthdate()) + "' WHERE id = " + user.getId() + ";");
            statement.executeUpdate("UPDATE " + DATABASE_NAME + ".address SET townid = (SELECT id FROM " + DATABASE_NAME
                    + ".town WHERE name = '" + selectedTown + "'), street = '" + address.getStreet()
                    + "', houseNr = '" + address.getHouseNr() + "' WHERE id = " + address.getId() + ";");
            statement.executeUpdate("COMMIT;");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            close(null, statement, connection);
        }
        return true;
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
