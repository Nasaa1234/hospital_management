package models;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;

import services.AppointmentService;
import services.UserLoader;

public class Patient extends User {
    String image;

    public Patient(String name, String email, String password, String image) {
        super(name, email, "patient", password);
        this.image = image;
    }

    public String getPatientName() {
        return name;
    }

    @Override
    public void accessPanel() {
        List<User> doctors = new ArrayList<>(UserLoader.loadUsers("database/doctors.csv"));

        JFrame frame = new JFrame("Patient Panel");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);

        JPanel rightPanel = new JPanel(new CardLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("APPOINTMENT MENU"));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel makeAppointmentPanel = new JPanel();
        makeAppointmentPanel.setLayout(new BoxLayout(makeAppointmentPanel, BoxLayout.Y_AXIS));
        makeAppointmentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel cancelAppointmentPanel = new JPanel();
        cancelAppointmentPanel.add(new JLabel("Cancel Appointment Screen"));

        JPanel historyPanel = new JPanel();
        historyPanel.add(new JLabel("History Screen"));

        JPanel timePanel = new JPanel(new GridLayout(0, 4, 10, 10));

        JScrollPane scrollPane = new JScrollPane(timePanel);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JLabel title = new JLabel("Make an Appointment");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel doctorLabel = new JLabel("Select Doctor:");

        List<String> doctorNames = new ArrayList<>();
        for (User doctor : doctors) {
            doctorNames.add(doctor.name);
        }
        String[] options = doctorNames.toArray(new String[0]);
        JComboBox<String> doctorSelect = new JComboBox<>(options);
        doctorSelect.setMaximumSize(new Dimension(350, 40));

        JButton okButton = new JButton("Make an appointment");

        makeAppointmentPanel.add(title);
        makeAppointmentPanel.add(Box.createVerticalStrut(10));
        makeAppointmentPanel.add(doctorLabel);
        makeAppointmentPanel.add(Box.createVerticalStrut(10));
        makeAppointmentPanel.add(doctorSelect);
        makeAppointmentPanel.add(Box.createVerticalStrut(10));

        makeAppointmentPanel.add(Box.createVerticalStrut(10));

        makeAppointmentPanel.add(new JLabel("Free time:"));
        makeAppointmentPanel.add(Box.createVerticalStrut(10));

        doctorSelect.addActionListener(e -> {
            String selectedDoctor = (String) doctorSelect.getSelectedItem();
            List<LocalDateTime> free = AppointmentService.getFreeTimeSlots(selectedDoctor, 60);

            timePanel.removeAll();
            timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));

            LocalDate currentLabelDate = null;
            JPanel daySection = null;

            for (LocalDateTime slot : free) {
                LocalDate slotDate = slot.toLocalDate();

                if (!slotDate.equals(currentLabelDate)) {
                    currentLabelDate = slotDate;

                    JLabel dayLabel = new JLabel(slotDate.toString());
                    dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    dayLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
                    timePanel.add(dayLabel);

                    daySection = new JPanel(new GridLayout(0, 5, 10, 10));
                    daySection.setAlignmentX(Component.LEFT_ALIGNMENT);
                    timePanel.add(daySection);
                }

                JButton timeButton = new JButton(slot.toLocalTime().toString());
                Color defaultColor = timeButton.getBackground();

                timeButton.addActionListener(e1 -> {
                    JButton clickedButton = (JButton) e1.getSource();
                    if (clickedButton.getBackground().equals(Color.GRAY)) {
                        clickedButton.setBackground(defaultColor);
                    } else {
                        clickedButton.setBackground(Color.GRAY);
                    }

                    clickedButton.setOpaque(true);
                    clickedButton.setBorderPainted(false);
                });
                if (daySection != null) {
                    daySection.add(timeButton);
                }
            }

            timePanel.revalidate();
            timePanel.repaint();
        });

        // daylabel, selectedDoctor, name, 


        

        makeAppointmentPanel.add(scrollPane, BorderLayout.CENTER);
        makeAppointmentPanel.add(Box.createVerticalStrut(10));

        makeAppointmentPanel.add(okButton);

        rightPanel.add(makeAppointmentPanel, "default");
        rightPanel.add(cancelAppointmentPanel, "cancelAppointment");
        rightPanel.add(historyPanel, "history");

        CardLayout cardLayout = (CardLayout) rightPanel.getLayout();

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createTitledBorder("MENU"));

        JButton makeAppointmentBtn = new JButton("1. Make an appointment");
        makeAppointmentBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JButton seeScheduleBtn = new JButton("2. Cancel an appointment");
        seeScheduleBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JButton historyBtn = new JButton("3. View History");
        historyBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JButton logoutBtn = new JButton("4. Log out");
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        leftPanel.add(new JLabel("<html><h1>MENU</h1></html>"));
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(makeAppointmentBtn);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(seeScheduleBtn);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(historyBtn);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(logoutBtn);

        makeAppointmentBtn.addActionListener(e -> {
            cardLayout.show(rightPanel, "default");
        });

        seeScheduleBtn.addActionListener(e -> {
            cardLayout.show(rightPanel, "cancelAppointment");
        });

        historyBtn.addActionListener(e -> {
            cardLayout.show(rightPanel, "history");
        });

        logoutBtn.addActionListener(e -> {
            frame.dispose();
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(0.1);
        splitPane.setResizeWeight(0.1);
        splitPane.setContinuousLayout(true);

        frame.add(splitPane);
        frame.setVisible(true);

    }
}
