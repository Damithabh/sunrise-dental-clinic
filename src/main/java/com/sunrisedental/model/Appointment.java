package com.sunrisedental.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a scheduled appointment at Sunrise Dental Clinic.
 * 
 * An appointment links a Patient to a Dentist for a specific Treatment
 * on a given date and time. Each appointment has a unique appointment number.
 * 
 * Relationships (from Class Diagram):
 *   - Composition: Appointment *-- Patient (appointment cannot exist without patient)
 *   - Composition: Appointment *-- Treatment (appointment cannot exist without treatment)
 *   - Aggregation: Appointment o-- Dentist (dentist exists independently)
 * 
 * Design Pattern: Part of the Domain Model pattern.
 */
public class Appointment {

    private String appointmentNumber;
    private Patient patient;
    private String dentistName;
    private Treatment treatment;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
    private String notes;

    // ── Constructors ──

    public Appointment() {
    }

    public Appointment(String appointmentNumber, Patient patient, String dentistName,
                       Treatment treatment, LocalDate appointmentDate,
                       LocalTime appointmentTime) {
        this.appointmentNumber = appointmentNumber;
        this.patient = patient;
        this.dentistName = dentistName;
        this.treatment = treatment;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = "SCHEDULED";
    }

    // ── Getters and Setters ──

    public String getAppointmentNumber() {
        return appointmentNumber;
    }

    public void setAppointmentNumber(String appointmentNumber) {
        this.appointmentNumber = appointmentNumber;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getDentistName() {
        return dentistName;
    }

    public void setDentistName(String dentistName) {
        this.dentistName = dentistName;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentNumber='" + appointmentNumber + '\'' +
                ", patient=" + (patient != null ? patient.getPatientName() : "null") +
                ", dentistName='" + dentistName + '\'' +
                ", treatment=" + (treatment != null ? treatment.getTreatmentType() : "null") +
                ", date=" + appointmentDate +
                ", time=" + appointmentTime +
                ", status='" + status + '\'' +
                '}';
    }
}
