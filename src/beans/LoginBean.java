package beans;

/**
 *
 * @author Markus
 */

import dto.User;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
public class LoginBean {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void check(ActionEvent event) throws IOException {
        String[] loginData = {getEmail(), getPassword()};
        User user = new User(loginData);

        for (User admin : getAdmins())
            if (admin.equals(user)) {
                System.out.println("ES LEEEEEBT!!!");
                return;
            }

        FacesMessage message = new FacesMessage("Username or Password wrong!");
        throw new ValidatorException(message);
    }

    private List<User> getAdmins() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(/*Files.ADMIN.getPath()*/""));

        List<User> admins = new ArrayList<>();

        String tmp;
        do {
            tmp = br.readLine();
            if (tmp == null)
                break;
            admins.add(new User(tmp.split(",")));
        } while (true);

        br.close();

        return admins;
    }
}
