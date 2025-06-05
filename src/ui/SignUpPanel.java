package ui;

import javax.swing.*;

import auth.Auth;
import models.User;
import services.UserLoader;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class SignUpPanel extends JPanel {

    public SignUpPanel() {
        List<User> users = new ArrayList<>();
        users.addAll(UserLoader.loadUsers("database/patients.csv"));
        users.addAll(UserLoader.loadUsers("database/doctors.csv"));
        users.addAll(UserLoader.loadUsers("database/admin.csv"));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel(
                "<html><div style='text-align:center;'>"
                        + "<h1 style='font-size:28px'>Create an Account</h1></div></html>",
                SwingConstants.CENTER);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nameField = new JTextField(25);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.setBorder(BorderFactory.createTitledBorder("Full Name"));

        JTextField emailField = new JTextField(25);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));

        JPasswordField pwdField = new JPasswordField(25);
        pwdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        pwdField.setAlignmentX(Component.CENTER_ALIGNMENT);
        pwdField.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton registerBtn = new JButton("Register");
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton signinBtn = new JButton("Login");
        signinBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        signinBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel noAccountLbl = new JLabel("—  or  —");
        noAccountLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(header);
        add(Box.createVerticalStrut(15));
        add(nameField);
        add(Box.createVerticalStrut(10));
        add(emailField);
        add(Box.createVerticalStrut(10));
        add(pwdField);
        add(Box.createVerticalStrut(13));
        add(registerBtn);
        add(Box.createVerticalStrut(15));
        add(noAccountLbl);
        add(Box.createVerticalStrut(10));
        add(signinBtn);

        signinBtn.addActionListener(e -> {
            Auth.signupFlow();
        });

        signinBtn.addActionListener(e -> {
            Container parent = SignUpPanel.this.getParent();
            parent.remove(SignUpPanel.this);
            parent.add(new SignInPanel((email, pwd) -> {
                Auth.loginFlow(email, pwd, users);
            },
                    (uList) -> {
                        Auth.signupFlow();
                    }), 1);

            parent.revalidate();
            parent.repaint();
        });

    }
}
