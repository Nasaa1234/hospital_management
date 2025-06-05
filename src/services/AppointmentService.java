package services;

import models.Appointment;
import models.Doctor;
import models.Patient;
import models.User;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class AppointmentService {

    private static List<Appointment> appointments;

    static {
        appointments = new ArrayList<>();
    }

    // just get one doctors patients waiting and appointment
    public static List<Appointment> loadDoctorAppointments(String FILE, String name) {
        List<Appointment> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4 && Objects.equals(name, data[0])) {
                    User doctorUser = UserLoader.getUserByName("database/doctors.csv", data[0]);
                    User patientUser = UserLoader.getUserByName("database/patients.csv", data[1]);

                    if (doctorUser instanceof Doctor && patientUser instanceof Patient) {
                        Doctor doctor = (Doctor) doctorUser;
                        Patient patient = (Patient) patientUser;

                        list.add(new Appointment(
                                doctor,
                                patient,
                                LocalDateTime.parse(data[2]),
                                data[3]));
                    } else {
                        System.out.println("Error: Doctor or Patient data not valid for " + data[0] + " or " + data[1]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("aldaa garlaa: " + e.getMessage());
        }
        return list;
    }

    public static List<LocalDateTime> getFreeTimeSlots(String doctorName, int slotMinutes) {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(17, 0);
        List<LocalDateTime> freeSlots = new ArrayList<>();

        Set<LocalDateTime> bookedSlots = new HashSet<>();

        for (Appointment appt : appointments) {
            if (appt.getDoctor().getDoctorName().equals(doctorName)) {
                bookedSlots.add(appt.getDateTime());
            }
        }

        // Check the next 7 days
        for (int dayOffset = 0; dayOffset < 7; dayOffset++) {
            LocalDate date = LocalDate.now().plusDays(dayOffset);
            for (LocalTime time = start; time.isBefore(end); time = time.plusMinutes(slotMinutes)) {
                LocalDateTime slot = LocalDateTime.of(date, time);
                if (!bookedSlots.contains(slot)) {
                    freeSlots.add(slot);
                }
            }
        }

        return freeSlots;
    }

    public static String makeAppointment(String doctorName, String patientName, LocalDateTime dateTime) {
        String appointmentId = UUID.randomUUID().toString();

        for (Appointment appt : appointments) {
            if (appt.getDoctor().getDoctorName().equals(doctorName)
                    && appt.getDateTime().equals(dateTime)) {
                return "This slot is already taken.";
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("database/waitingSchedule.csv", true))) {
            String line = String.join(",", doctorName, patientName, dateTime.toString(), appointmentId);
            bw.newLine();
            bw.write(line);
            User doctorUser = UserLoader.getUserByName("database/doctors.csv", doctorName);
            User patientUser = UserLoader.getUserByName("database/patients.csv", patientName);

            if (doctorUser instanceof Doctor && patientUser instanceof Patient) {
                Doctor doctorInfo = (Doctor) doctorUser;
                Patient patientInfo = (Patient) patientUser;

                appointments.add(new Appointment(doctorInfo, patientInfo, dateTime, appointmentId));
            } else {
                System.out.println("Error: Doctor or Patient data not valid for " + doctorName + " or " + patientName);
            }

        } catch (IOException e) {
            System.out.println("Error making an appointment: " + e.getMessage());
            return "Failed to book appointment.";
        }

        return "Appointment booked successfully!";
    }

    public static List<Appointment> getAllAppointments() {
        return appointments;
    }
}
