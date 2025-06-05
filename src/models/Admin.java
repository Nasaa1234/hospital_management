package models;

import ui.HomePanel;

import javax.swing.*;

import services.UserLoader;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Admin extends User {

    public Admin(String name, String email, String password) {
        super(name, email, "admin", password);
    }

    @Override
    public void accessPanel() {
        List<User> users = new ArrayList<>();
        users.addAll(UserLoader.loadUsers("database/patients.csv"));
        users.addAll(UserLoader.loadUsers("database/doctors.csv"));
        users.addAll(UserLoader.loadUsers("database/admin.csv"));

        JFrame frame = new JFrame("Admin Panel");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 450);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel(
                "<html><div style='text-align:center;'><h1>Welcome to Admin Panel, " + name + "</h1></div></html>",
                SwingConstants.CENTER);
        panel.add(welcomeLabel, BorderLayout.NORTH);

        List<User> doctors = UserLoader.loadUsers("database/doctors.csv");

        JPanel grid = new JPanel(new GridLayout(0, 5, 12, 12));
        grid.setOpaque(false);

        for (User u : doctors) {
            if (u instanceof Doctor) {
                grid.add(createDoctorCard((Doctor) u, frame));
            }
        }

        JScrollPane scroller = new JScrollPane(
                grid,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroller, BorderLayout.CENTER);

        JButton logoutBtn = new JButton("Log out");
        logoutBtn.setPreferredSize(new Dimension(200, 40));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to log out?",
                    "Log Out",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                frame.getContentPane().removeAll();
                frame.add(new HomePanel(frame));
                frame.revalidate();
                frame.repaint();
            }
        });

        JButton addDoctor = new JButton("Add Doctor");
        addDoctor.setPreferredSize(new Dimension(200, 40));
        addDoctor.addActionListener(e -> {
            System.out.println("add doctro");
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(logoutBtn);
        bottomPanel.add(addDoctor);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private JPanel createDoctorCard(Doctor doctor, JFrame frame) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDDDDDD), 1, true),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        card.setBackground(Color.WHITE);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setMaximumSize(new Dimension(140, 200));

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(0xF6F8FF));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showDoctorDetails(doctor, frame);
            }
        });

        ImageIcon raw = new ImageIcon("assets/doctors/doctor.jpeg");
        Image img = raw.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        JLabel picLbl = new JLabel(new ImageIcon(img));
        picLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        picLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        JLabel nameLbl = new JLabel(doctor.name);
        nameLbl.setFont(nameLbl.getFont().deriveFont(Font.BOLD, 14f));
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel mailLbl = new JLabel(doctor.email);
        mailLbl.setFont(mailLbl.getFont().deriveFont(Font.PLAIN, 12f));
        mailLbl.setForeground(Color.DARK_GRAY);
        mailLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel freeTimeLbl = new JLabel("Free Time: 1");
        freeTimeLbl.setFont(freeTimeLbl.getFont().deriveFont(Font.PLAIN, 12f));
        freeTimeLbl.setForeground(Color.DARK_GRAY);
        freeTimeLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(picLbl);
        card.add(nameLbl);
        card.add(mailLbl);
        card.add(freeTimeLbl);

        return card;
    }

    private void showDoctorDetails(Doctor doctor, JFrame frame) {
        JFrame detailFrame = new JFrame("Doctor Details");
        detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        detailFrame.setSize(300, 200);
        detailFrame.setLocationRelativeTo(frame);

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel doctorDetails = new JLabel(
                "<html>Name: " + doctor.name + "<br>Email: " + doctor.email + "</html>");
        doctorDetails.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailPanel.add(doctorDetails);
        detailPanel.add(Box.createVerticalStrut(20));

        JButton deleteBtn = new JButton("Delete Doctor");
        deleteBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteBtn.setPreferredSize(new Dimension(200, 30));
        deleteBtn.addActionListener(e -> {
            int confirmation = JOptionPane.showConfirmDialog(
                    detailFrame,
                    "Are you sure you want to delete this doctor?",
                    "Delete Doctor",
                    JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                deleteDoctor(doctor);
                detailFrame.dispose();
                JOptionPane.showMessageDialog(frame, "Doctor deleted successfully.");
            }
        });

        detailPanel.add(deleteBtn);
        detailFrame.add(detailPanel);
        detailFrame.setVisible(true);
    }

    private void deleteDoctor(Doctor doctor) {
        List<User> users = UserLoader.loadUsers("database/doctors.csv");
        users.removeIf(user -> user.name.equals(doctor.name) && user.email.equals(doctor.email));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("database/doctors.csv"))) {
            writer.write("name,email,role,password");
            writer.newLine();

            for (User user : users) {
                writer.write(user.name + "," + user.email + "," + user.role + "," + user.password);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error deleting doctor: " + e.getMessage());
        }
    }
}
