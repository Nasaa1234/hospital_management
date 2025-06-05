package ui;

import models.User;
import services.UserLoader;
import auth.Auth;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HomePanel extends JPanel {

    public HomePanel(JFrame frame) {

        List<User> users = new ArrayList<>();
        users.addAll(UserLoader.loadUsers("database/patients.csv"));
        users.addAll(UserLoader.loadUsers("database/doctors.csv"));
        users.addAll(UserLoader.loadUsers("database/admin.csv"));

        setLayout(new GridLayout(1, 2));

        ImageIcon rawIcon = new ImageIcon("assets/logo/doctor.jpeg");
        Image img = rawIcon.getImage().getScaledInstance(380, 380, Image.SCALE_SMOOTH);
        JLabel imgLbl = new JLabel(new ImageIcon(img), SwingConstants.CENTER);

        SignInPanel signIn = new SignInPanel(
                (email, pwd) -> {
                    frame.dispose();
                    Auth.loginFlow(email, pwd, users);
                },
                (uList) -> {
                    frame.dispose();
                    Auth.signupFlow();
                });

        add(imgLbl);
        add(signIn);
    }
}
