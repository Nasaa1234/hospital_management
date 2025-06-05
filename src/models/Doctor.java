package models;

import javax.swing.*;

import services.AppointmentService;
import services.UserLoader;

import java.awt.*;
import java.util.List;

public class Doctor extends User {
    private JPanel rightPanel;

    public Doctor(String name, String email, String password) {
        super(name, email, "doctor", password);
    }

    public String getDoctorName() {
        return name;
    }

    @Override
    public void accessPanel() {
        System.out.println(name);
        List<Appointment> upComingPatients = AppointmentService.loadDoctorAppointments("database/schedule.csv",
                name);
        List<Appointment> waitingPatients = AppointmentService.loadDoctorAppointments("database/waitingSchedule.csv",
                name);

        JFrame frame = new JFrame("Doctor Panel");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 450);
        frame.setLocationRelativeTo(null);

        rightPanel = new JPanel();
        rightPanel.setBorder(BorderFactory.createTitledBorder("DOCTOR TIME"));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createTitledBorder("WAITING TIME"));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        List<User> patients = UserLoader.loadUsers("database/patients.csv");

        JPanel leftUpPanel = new JPanel();
        leftUpPanel.setLayout(new BoxLayout(leftUpPanel, BoxLayout.X_AXIS));
        leftUpPanel.setBorder(BorderFactory.createTitledBorder("Patient Cards"));

        for (Appointment u : upComingPatients) {
            if (u instanceof Appointment) {
                leftUpPanel.add(createPatientCard((Appointment) u, frame));
                leftUpPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            }
        }

        int cardWidth = 180;
        int panelWidth = Math.max(cardWidth * patients.size(), 400);
        leftUpPanel.setPreferredSize(new Dimension(panelWidth, 220));

        JScrollPane scrollUpPane = new JScrollPane(leftUpPanel);
        scrollUpPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollUpPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollUpPane.getHorizontalScrollBar().setUnitIncrement(16);

        JPanel leftDownPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        for (Appointment u : waitingPatients) {
            if (u instanceof Appointment) {
                leftDownPanel.add(createWaitingPatient((Appointment) u, frame));
            }
        }

        JScrollPane scrollDownPane = new JScrollPane(leftDownPanel);
        scrollDownPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollDownPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollDownPane.getVerticalScrollBar().setUnitIncrement(16);

        rightPanel.add(new JLabel("Welcome to the doctor panel"));

        JScrollPane scrollDetail = new JScrollPane(rightPanel);
        scrollDetail.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollDetail.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollDetail.getVerticalScrollBar().setUnitIncrement(16);

        JSplitPane splitLeftPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollUpPane, scrollDownPane);
        splitLeftPane.setResizeWeight(0.5);

        JSplitPane splitMainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitLeftPane, scrollDetail);
        splitMainPane.setResizeWeight(0.5);

        SwingUtilities.invokeLater(() -> {
            splitMainPane.setDividerLocation(frame.getWidth() / 2);
        });

        frame.add(splitMainPane);
        frame.setVisible(true);
    }

    private JPanel createPatientCard(Appointment appt, JFrame frame) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDDDDDD), 1, true),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        card.setBackground(Color.WHITE);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(160, 120));
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

            public void mouseClicked(java.awt.event.MouseEvent e) {
                rightPanel.removeAll();
                rightPanel.add(SeePatientDetail(appt.getPatient(), frame));
                rightPanel.revalidate();
                rightPanel.repaint();

            }
        });

        ImageIcon raw = new ImageIcon(appt.getPatient().image);
        Image img = raw.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        JLabel picLbl = new JLabel(new ImageIcon(img));
        picLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        picLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        JLabel nameLbl = new JLabel(appt.getPatient().name);
        nameLbl.setFont(nameLbl.getFont().deriveFont(Font.BOLD, 14f));
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel mailLbl = new JLabel(appt.getPatient().email);
        mailLbl.setFont(mailLbl.getFont().deriveFont(Font.PLAIN, 12f));
        mailLbl.setForeground(Color.DARK_GRAY);
        mailLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(picLbl);
        card.add(nameLbl);
        card.add(mailLbl);

        return card;
    }

    private JPanel createWaitingPatient(Appointment appt, JFrame frame) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDDDDDD), 1, true),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        card.setBackground(Color.WHITE);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setMaximumSize(new Dimension(350, 130));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel numberAndNameLabel = new JLabel(appt.getPatient().getPatientName());
        numberAndNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        numberAndNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel emailLabel = new JLabel(appt.getPatient().email);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        emailLabel.setForeground(Color.GRAY);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(numberAndNameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(emailLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        JButton approveButton = new JButton("Approve");
        approveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        approveButton.setFocusable(false);

        JButton rejectButton = new JButton("Reject");
        rejectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rejectButton.setFocusable(false);

        buttonPanel.add(approveButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        buttonPanel.add(rejectButton);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    private JPanel SeePatientDetail(Patient patient, JFrame frame) {
        JPanel detail = new JPanel();
        detail.setLayout(new BoxLayout(detail, BoxLayout.Y_AXIS));
        detail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDDDDDD), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        detail.setBackground(Color.WHITE);
        detail.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ImageIcon raw = new ImageIcon(patient.image);
        Image img = raw.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
        JLabel picLbl = new JLabel(new ImageIcon(img));
        picLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        picLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel nameLbl = new JLabel(patient.name);
        nameLbl.setFont(nameLbl.getFont().deriveFont(Font.BOLD, 20f));
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel mailLbl = new JLabel(patient.email);
        mailLbl.setFont(mailLbl.getFont().deriveFont(Font.PLAIN, 14f));
        mailLbl.setForeground(Color.DARK_GRAY);
        mailLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel diseaseLabel = new JLabel("Disease:");
        diseaseLabel.setFont(diseaseLabel.getFont().deriveFont(Font.PLAIN, 13f));
        diseaseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField disease = new JTextField(20);
        disease.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        disease.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel notesLabel = new JLabel("Notes:");
        notesLabel.setFont(notesLabel.getFont().deriveFont(Font.PLAIN, 13f));
        notesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea diagnosisArea = new JTextArea(4, 20);
        diagnosisArea.setLineWrap(true);
        diagnosisArea.setWrapStyleWord(true);
        diagnosisArea.setFont(new Font("Arial", Font.PLAIN, 13));
        diagnosisArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane notes = new JScrollPane(diagnosisArea);
        notes.setAlignmentX(Component.CENTER_ALIGNMENT);
        notes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JButton doneButton = new JButton("DONE");
        doneButton.setMaximumSize(new Dimension(400, 20));
        doneButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        doneButton.addActionListener(e -> {
            System.out.println("add doctro");
        });

        detail.add(picLbl);
        detail.add(nameLbl);
        detail.add(Box.createRigidArea(new Dimension(0, 8)));
        detail.add(mailLbl);
        detail.add(Box.createRigidArea(new Dimension(0, 20)));
        detail.add(diseaseLabel);
        detail.add(disease);
        detail.add(Box.createRigidArea(new Dimension(0, 12)));
        detail.add(notesLabel);
        detail.add(notes);

        detail.add(Box.createRigidArea(new Dimension(0, 12)));
        detail.add(doneButton);

        return detail;
    }

}
