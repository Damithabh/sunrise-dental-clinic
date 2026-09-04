package com.sunrisedental.model;


/**
 * Represents a dental treatment type offered by Sunrise Dental Clinic.
 * 
 * Each treatment has a fixed cost stored in the database.
 * This is a core domain entity in the Business Logic Tier.
 * 
 * Design Pattern: Part of the Domain Model pattern.
 */
public class Treatment {

    private int treatmentId;
    private String treatmentType;
    private String description;
    private double treatmentCost;
    private int durationMinutes;

    // ── Constructors ──

    public Treatment() {
    }

    public Treatment(int treatmentId, String treatmentType, double treatmentCost) {
        this.treatmentId = treatmentId;
        this.treatmentType = treatmentType;
        this.treatmentCost = treatmentCost;
    }

    public Treatment(int treatmentId, String treatmentType, String description,
                     double treatmentCost, int durationMinutes) {
        this.treatmentId = treatmentId;
        this.treatmentType = treatmentType;
        this.description = description;
        this.treatmentCost = treatmentCost;
        this.durationMinutes = durationMinutes;
    }

    // ── Getters and Setters ──

    public int getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(int treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getTreatmentType() {
        return treatmentType;
    }

    public void setTreatmentType(String treatmentType) {
        this.treatmentType = treatmentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTreatmentCost() {
        return treatmentCost;
    }

    public void setTreatmentCost(double treatmentCost) {
        this.treatmentCost = treatmentCost;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @Override
    public String toString() {
        return "Treatment{" +
                "treatmentId=" + treatmentId +
                ", treatmentType='" + treatmentType + '\'' +
                ", treatmentCost=" + treatmentCost +
                '}';
    }
}
