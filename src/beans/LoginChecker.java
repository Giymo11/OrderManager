package beans;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Markus
 */

import pojo.User;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginChecker {
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

    public void check(ActionEvent event) throws FileNotFoundException, IOException {
        FileReader reader = new FileReader("admins.txt");
        BufferedReader br = new BufferedReader(reader);

        String tmp = null;
        List<User> admins = new ArrayList<User>();

        do {
            tmp = br.readLine();
            System.out.println(tmp);
            if (tmp == null)
                break;
            admins.add(new User(tmp.split(",")));
        } while (true);

        String[] loginData = new String[2];
        loginData[0] = this.email;
        loginData[1] = this.password;
        User user = new User(loginData);
        System.out.println(user.toString());

        for (User admin : admins) {
            System.out.println(admin.toString());
            if (admin.equals(user)) {
                System.out.println("ES LEEEEEBT!!!");
                return;
            }
        }

        FacesMessage message = new FacesMessage("Username or Password wrong!");
        throw new ValidatorException(message);
    }
}
