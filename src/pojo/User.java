package pojo;

import java.util.Arrays;

/**
 * @author Markus
 */
public class User {
    private String[] loginData;

    public String[] getLoginData() {
        return loginData;
    }

    public String getUsername() {
        return loginData[0];
    }

    public String getPassword() {
        return loginData[1];
    }

    public User(String[] loginData) {
        this.loginData = loginData;
    }

    @Override
    public String toString() {
        return loginData[0] + ", " + loginData[1];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return loginData[0].equals(other.loginData[0]) && loginData[1].equals(other.loginData[1]);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Arrays.deepHashCode(this.loginData);
        return hash;
    }
}
