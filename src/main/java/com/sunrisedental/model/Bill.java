package com.sunrisedental.model;

import java.time.LocalDate;

/**
 * Represents a generated bill/invoice for a completed appointment.
 * 
 * A Bill is composed of a consultation fee plus the treatment cost,
 * with an optional discount. The total is calculated by BillingService.
 * 
 * Relationship: Bill *-- Appointment (composition — bill cannot exist without appointment)
 * 
 * Design Pattern: Part of the Domain Model pattern.
 */
public class Bill {

    private int billId;
    private Appointment appointment;
    private double consultationFee;
    private double treatmentCost;
    private double discountPercentage;
    private double totalAmount;
    private LocalDate billDate;
    private String paymentStatus;

    // ── Constructors ──

    public Bill() {
        this.billDate = LocalDate.now();
        this.paymentStatus = "UNPAID";
        this.discountPercentage = 0.0;
    }

    public Bill(Appointment appointment, double consultationFee, double treatmentCost) {
        this();
        this.appointment = appointment;
        this.consultationFee = consultationFee;
        this.treatmentCost = treatmentCost;
        this.totalAmount = consultationFee + treatmentCost;
    }

    // ── Business Methods ──

    /**
     * Recalculates the total amount after a discount has been applied.
     * Total = (consultationFee + treatmentCost) * (1 - discountPercentage / 100)
     */
    public void recalculateTotal() {
        double subtotal = this.consultationFee + this.treatmentCost;
        if (this.discountPercentage > 0) {
            this.totalAmount = subtotal * (1 - this.discountPercentage / 100.0);
        } else {
            this.totalAmount = subtotal;
        }
    }

    /**
     * Generates a formatted bill receipt string.
     * 
     * @return formatted receipt string
     */
    public String generateReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("═══════════════════════════════════════\n");
        receipt.append("    SUNRISE DENTAL CLINIC\n");
        receipt.append("    Patient Bill / Receipt\n");
        receipt.append("═══════════════════════════════════════\n");
        receipt.append(String.format("Bill ID        : %d%n", billId));
        receipt.append(String.format("Bill Date      : %s%n", billDate));

        if (appointment != null) {
            receipt.append(String.format("Appointment No : %s%n", appointment.getAppointmentNumber()));
            if (appointment.getPatient() != null) {
                receipt.append(String.format("Patient Name   : %s%n", appointment.getPatient().getPatientName()));
            }
            receipt.append(String.format("Dentist        : %s%n", appointment.getDentistName()));
            if (appointment.getTreatment() != null) {
                receipt.append(String.format("Treatment      : %s%n", appointment.getTreatment().getTreatmentType()));
            }
        }

        receipt.append("───────────────────────────────────────\n");
        receipt.append(String.format("Consultation Fee : LKR %,.2f%n", consultationFee));
        receipt.append(String.format("Treatment Cost   : LKR %,.2f%n", treatmentCost));

        if (discountPercentage > 0) {
            receipt.append(String.format("Discount         : %.1f%%%n", discountPercentage));
        }

        receipt.append("───────────────────────────────────────\n");
        receipt.append(String.format("TOTAL AMOUNT     : LKR %,.2f%n", totalAmount));
        receipt.append(String.format("Payment Status   : %s%n", paymentStatus));
        receipt.append("═══════════════════════════════════════\n");

        return receipt.toString();
    }

    // ── Getters and Setters ──

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public double getTreatmentCost() {
        return treatmentCost;
    }

    public void setTreatmentCost(double treatmentCost) {
        this.treatmentCost = treatmentCost;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", totalAmount=" + totalAmount +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
