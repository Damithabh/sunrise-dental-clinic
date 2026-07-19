package com.sunrisedental.model;

/**
 * Represents a registered patient at Sunrise Dental Clinic.
 * 
 * Stores demographic information collected during patient registration.
 * A patient can have multiple appointments (1-to-many relationship).
 * 
 * Design Pattern: Part of the Domain Model pattern.
 */
public class Patient {

    private int patientId;
    private String patientName;
    private String address;
    private String contactNumber;
    private String email;

    // ── Constructors ──

    public Patient() {
    }

    public Patient(int patientId, String patientName, String address, String contactNumber) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public Patient(int patientId, String patientName, String address,
                   String contactNumber, String email) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    // ── Getters and Setters ──

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", patientName='" + patientName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}
