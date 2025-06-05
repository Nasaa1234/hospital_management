package models;

import java.time.LocalDateTime;

//write appointment time [using doctor, patient class]
public class Appointment {
    private Doctor doctor;
    private Patient patient;
    private LocalDateTime dateTime;
    private String id;

    public Appointment(Doctor doctor, Patient patient, LocalDateTime dateTime, String id) {
        this.doctor = doctor;
        this.patient = patient;
        this.dateTime = dateTime;
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getId() {
        return id;
    }

    public String toCSV() {
        return doctor.getDoctorName() + "," + patient.getPatientName() + "," + dateTime + "," + id;
    }
}
