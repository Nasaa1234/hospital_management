package auth;

import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;
import models.User;

public class Auth {
    public static void loginFlow(String email, String password, List<User> users) {
        for (User u : users) {
            System.out.println(u.name);
        }
        if (email == null || email.equalsIgnoreCase("exit")) {
            JOptionPane.showMessageDialog(null, "Thank you, bye!");
            System.exit(0);
        }

        Optional<User> optionalUser = users.stream()
                .filter(u -> u.email.equals(email))
                .findFirst();

        if (optionalUser.isEmpty()) {
            JOptionPane.showMessageDialog(null, "User not found. Please try again.");
        }

        User user = optionalUser.get();

        if (!user.checkPassword(password)) {
            JOptionPane.showMessageDialog(null, "Wrong password. Please try again.");
        }

        user.accessPanel();
    }

    public static void signupFlow() {
        // JPanel panel = new JPanel();
        // panel.setPreferredSize(new Dimension(400, 130));

        // panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // JTextField nameField = new JTextField(15);
        // JTextField emailField = new JTextField(15);
        // JPasswordField passwordField = new JPasswordField(15);

        // panel.add(new JLabel("Name:"));
        // panel.add(nameField);
        // panel.add(Box.createVerticalStrut(10));
        // panel.add(new JLabel("Email:"));
        // panel.add(emailField);
        // panel.add(Box.createVerticalStrut(10));
        // panel.add(new JLabel("Password:"));
        // panel.add(passwordField);

        // int result = JOptionPane.showConfirmDialog(null, panel,
        // "Sign Up", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // if (result == JOptionPane.OK_OPTION) {
        // String name = nameField.getText().trim();
        // String email = emailField.getText().trim();
        // String password = new String(passwordField.getPassword());

        // if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
        // JOptionPane.showMessageDialog(null, "All fields must be filled out!",
        // "Error",
        // JOptionPane.ERROR_MESSAGE);
        // return;
        // }

        // if (!email.contains("@") || !email.contains(".")) {
        // JOptionPane.showMessageDialog(null, "Invalid email format!", "Error",
        // JOptionPane.ERROR_MESSAGE);
        // return;
        // }

        // if (password.length() < 6) {
        // JOptionPane.showMessageDialog(null, "Password must be at least 6 characters
        // long!", "Error",
        // JOptionPane.ERROR_MESSAGE);
        // return;
        // }

        // UserLoader.addUser(name, email, "patient", password);
        // JOptionPane.showMessageDialog(null, "Account created successfully!");
        // }
        System.out.println("ASDF    ");

    }

}
