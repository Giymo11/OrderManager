package dto;

/**
 * @author Markus
 */
public class User {
    private String email, password;

    public User(String[] loginData) {
        email = loginData[0];
        password = loginData[1];
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getEmail() {

        return email;
    }

    public String getPassword() {
        return password;
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
        return email.equals(other.email) && password.equals(other.password);
    }
}
