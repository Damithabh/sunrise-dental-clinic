package com.sunrisedental.service;

import com.sunrisedental.exception.AppointmentNotFoundException;
import com.sunrisedental.model.Appointment;
import com.sunrisedental.model.Bill;

/**
 * BillingService — Business Logic Tier.
 * 
 * Handles all billing operations for the Sunrise Dental Clinic system.
 * This class was implemented as part of the TDD "GREEN" phase — written
 * specifically to make the BillingServiceTest test cases pass.
 * 
 * Responsibilities:
 *   - Calculate a bill for a given appointment (consultation fee + treatment cost)
 *   - Apply optional discounts to a bill
 *   - Validate billing preconditions (appointment must exist, treatment must be assigned)
 * 
 * Design Pattern: Facade — simplifies billing operations for the Presentation Tier.
 * Architecture:    Business Logic Tier (sits between Controller and DAO layers).
 * 
 * Assumptions:
 *   - Consultation fee is fixed at LKR 500.00 for all appointments
 *   - Treatment cost is retrieved from the Treatment object linked to the appointment
 *   - Discounts are optional and must be between 0% and 100% inclusive
 * 
 * @author Sunrise Dental Clinic Development Team
 * @version 1.0.0
 */
public class BillingService {

    /**
     * Fixed consultation fee applied to every appointment (in LKR).
     * This value is constant across all treatment types.
     */
    private static final double CONSULTATION_FEE = 500.00;

    /**
     * Calculates a bill for the given appointment.
     * 
     * The total is computed as: Consultation Fee + Treatment Cost.
     * 
     * Preconditions:
     *   - appointment must not be null
     *   - appointment must have a valid appointment number
     *   - appointment must have a treatment assigned
     * 
     * @param appointment the appointment to generate a bill for
     * @return a new Bill object with the calculated total
     * @throws AppointmentNotFoundException if appointment is null or has no appointment number
     * @throws IllegalArgumentException if the appointment has no treatment assigned
     */
    public Bill calculateBill(Appointment appointment) {
        // Validate: appointment must not be null
        if (appointment == null) {
            throw new AppointmentNotFoundException("null");
        }

        // Validate: appointment number must exist
        if (appointment.getAppointmentNumber() == null
                || appointment.getAppointmentNumber().trim().isEmpty()) {
            throw new AppointmentNotFoundException("empty");
        }

        // Validate: treatment must be assigned to the appointment
        if (appointment.getTreatment() == null) {
            throw new IllegalArgumentException(
                    "Cannot calculate bill: No treatment assigned to appointment "
                    + appointment.getAppointmentNumber());
        }

        // Calculate: Retrieve treatment cost from the appointment's treatment
        double treatmentCost = appointment.getTreatment().getTreatmentCost();

        // Create and return the Bill
        Bill bill = new Bill(appointment, CONSULTATION_FEE, treatmentCost);
        bill.setBillDate(java.time.LocalDate.now());
        bill.setPaymentStatus("UNPAID");

        return bill;
    }

    /**
     * Applies a percentage discount to an existing bill and recalculates the total.
     * 
     * Formula: Total = (Consultation Fee + Treatment Cost) × (1 - discount / 100)
     * 
     * @param bill the bill to apply the discount to
     * @param discountPercentage the discount percentage (0 to 100 inclusive)
     * @return the updated bill with the discount applied
     * @throws IllegalArgumentException if discount is negative or greater than 100
     * @throws IllegalArgumentException if bill is null
     */
    public Bill applyDiscount(Bill bill, double discountPercentage) {
        // Validate: bill must not be null
        if (bill == null) {
            throw new IllegalArgumentException("Bill cannot be null");
        }

        // Validate: discount must be between 0 and 100 inclusive
        if (discountPercentage < 0) {
            throw new IllegalArgumentException(
                    "Discount percentage cannot be negative: " + discountPercentage);
        }
        if (discountPercentage > 100) {
            throw new IllegalArgumentException(
                    "Discount percentage cannot exceed 100%: " + discountPercentage);
        }

        // Apply discount and recalculate
        bill.setDiscountPercentage(discountPercentage);
        bill.recalculateTotal();

        return bill;
    }

    /**
     * Returns the fixed consultation fee.
     * 
     * @return consultation fee in LKR
     */
    public double getConsultationFee() {
        return CONSULTATION_FEE;
    }
}
