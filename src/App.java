import ui.HomePanel;
import javax.swing.*;

import models.Admin;
import models.Doctor;
import models.Patient;

public class App {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Admin nasaa = new Admin("nasaa", "hello@gmail.com", "123");
        // Patient nasaa = new Patient("nasaa", "hello@gmail.com", "123");
        // Doctor nasaa = new Doctor("nasaa", "hello@gmail.com", "123");
        // nasaa.accessPanel();
        JFrame frame = new JFrame("Nas Hospital");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 450);
        frame.setLocationRelativeTo(null);

        frame.add(new HomePanel(frame));
        frame.setVisible(true);
    }
}
