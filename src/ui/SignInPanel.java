package ui;

import models.User;

import javax.swing.*;

import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SignInPanel extends JPanel {

    private final JTextField emailField;
    private final JPasswordField pwdField;

    public SignInPanel(BiConsumer<String, String> onLogin,
            Consumer<List<User>> onSignup) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcome = new JLabel(
                "<html><div style='text-align:center;'>"
                        + "<h1 style='font-size:28px'>Welcome to Nas Hospital</h1>"
                        + "</div></html>",
                SwingConstants.CENTER);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = new JTextField(25);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));

        pwdField = new JPasswordField(25);
        pwdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        pwdField.setAlignmentX(Component.CENTER_ALIGNMENT);
        pwdField.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton submitBtn = new JButton("Log In");
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        signupBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel noAccountLbl = new JLabel("—  or  —");
        noAccountLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(welcome);
        add(Box.createVerticalStrut(10));
        add(emailField);
        add(Box.createVerticalStrut(10));
        add(pwdField);
        add(Box.createVerticalStrut(20));
        add(submitBtn);
        add(Box.createVerticalStrut(15));
        add(noAccountLbl);
        add(Box.createVerticalStrut(10));
        add(signupBtn);

        Runnable tryLogin = () -> {
            String email = emailField.getText().trim();
            String pwd = new String(pwdField.getPassword());
            onLogin.accept(email, pwd);
        };

        submitBtn.addActionListener(e -> tryLogin.run());
        pwdField.addActionListener(e -> tryLogin.run());

        signupBtn.addActionListener(e -> {
            Container parent = SignInPanel.this.getParent();
            parent.remove(SignInPanel.this);
            parent.add(new SignUpPanel(), 1);

            parent.revalidate();
            parent.repaint();
        });

    }
}
