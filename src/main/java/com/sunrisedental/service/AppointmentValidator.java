package com.sunrisedental.service;

import com.sunrisedental.exception.InvalidAppointmentException;
import com.sunrisedental.model.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * AppointmentValidator — Business Logic Tier.
 * 
 * Validates appointment data before it is persisted to the database.
 * This class was implemented as part of the TDD "GREEN" phase — written
 * specifically to make the AppointmentValidatorTest test cases pass.
 * 
 * Validation Rules:
 *   1. Patient name must not be null, empty, or whitespace-only
 *   2. Contact number must be a valid 10-digit Sri Lankan number starting with 0
 *   3. Appointment date must not be in the past (today is allowed)
 *   4. Appointment time must be within clinic operating hours (08:00 – 20:00)
 *   5. Dentist name must not be null or empty
 *   6. Treatment must not be null
 * 
 * Design Pattern: Strategy — validation logic is encapsulated in a dedicated class,
 *                 separable from the service that uses it.
 * Architecture:   Business Logic Tier (invoked by AppointmentService before persistence).
 * 
 * @author Sunrise Dental Clinic Development Team
 * @version 1.0.0
 */
public class AppointmentValidator {

    /** Clinic opening time — appointments cannot be earlier than this. */
    private static final LocalTime CLINIC_OPENING_TIME = LocalTime.of(8, 0);

    /** Clinic closing time — appointments cannot be later than this. */
    private static final LocalTime CLINIC_CLOSING_TIME = LocalTime.of(20, 0);

    // ═══════════════════════════════════════════════════════════════
    //  INDIVIDUAL FIELD VALIDATORS
    // ═══════════════════════════════════════════════════════════════

    /**
     * Validates a patient name.
     * A valid name must be non-null, non-empty, and not whitespace-only.
     * 
     * @param patientName the name to validate
     * @return true if the name is valid, false otherwise
     */
    public boolean isValidPatientName(String patientName) {
        return patientName != null && !patientName.trim().isEmpty();
    }

    /**
     * Validates a Sri Lankan contact number.
     * Must be exactly 10 digits and start with '0'.
     * 
     * Valid formats: 0771234567, 0112345678
     * Invalid formats: null, "ABC", "077123", "1771234567"
     * 
     * @param contactNumber the contact number to validate
     * @return true if the number is valid, false otherwise
     */
    public boolean isValidContactNumber(String contactNumber) {
        if (contactNumber == null || contactNumber.trim().isEmpty()) {
            return false;
        }
        // Must be exactly 10 digits and start with 0
        return contactNumber.matches("^0\\d{9}$");
    }

    /**
     * Validates an appointment date.
     * The date must not be null and must not be in the past.
     * Today's date is considered valid (same-day appointments are allowed).
     * 
     * @param appointmentDate the date to validate
     * @return true if the date is valid (today or future), false otherwise
     */
    public boolean isValidAppointmentDate(LocalDate appointmentDate) {
        if (appointmentDate == null) {
            return false;
        }
        // Date must be today or in the future
        return !appointmentDate.isBefore(LocalDate.now());
    }

    /**
     * Validates an appointment time against clinic operating hours.
     * The clinic operates from 08:00 to 20:00 daily.
     * 
     * @param appointmentTime the time to validate
     * @return true if the time is within clinic hours, false otherwise
     */
    public boolean isValidAppointmentTime(LocalTime appointmentTime) {
        if (appointmentTime == null) {
            return false;
        }
        // Time must be >= 08:00 and <= 20:00
        return !appointmentTime.isBefore(CLINIC_OPENING_TIME)
                && !appointmentTime.isAfter(CLINIC_CLOSING_TIME);
    }

    /**
     * Validates a dentist name.
     * A valid name must be non-null, non-empty, and not whitespace-only.
     * 
     * @param dentistName the dentist name to validate
     * @return true if the name is valid, false otherwise
     */
    public boolean isValidDentistName(String dentistName) {
        return dentistName != null && !dentistName.trim().isEmpty();
    }

    // ═══════════════════════════════════════════════════════════════
    //  FULL APPOINTMENT VALIDATION
    // ═══════════════════════════════════════════════════════════════

    /**
     * Performs comprehensive validation on an entire Appointment object.
     * Checks all fields against their respective validation rules and
     * collects all errors into a list.
     * 
     * @param appointment the appointment to validate
     * @return a list of validation error messages (empty list if all valid)
     * @throws InvalidAppointmentException if the appointment object itself is null
     */
    public List<String> validateAppointment(Appointment appointment) {
        // Null appointment is a critical error — throw exception
        if (appointment == null) {
            throw new InvalidAppointmentException("Appointment object cannot be null");
        }

        List<String> errors = new ArrayList<>();

        // Validate patient name
        if (appointment.getPatient() == null
                || !isValidPatientName(appointment.getPatient().getPatientName())) {
            errors.add("Patient name is required and must not be empty");
        }

        // Validate contact number
        if (appointment.getPatient() != null
                && !isValidContactNumber(appointment.getPatient().getContactNumber())) {
            errors.add("Contact number must be a valid 10-digit Sri Lankan number starting with 0");
        }

        // Validate dentist name
        if (!isValidDentistName(appointment.getDentistName())) {
            errors.add("Dentist name is required and must not be empty");
        }

        // Validate treatment
        if (appointment.getTreatment() == null) {
            errors.add("Treatment type must be selected for the appointment");
        }

        // Validate appointment date
        if (!isValidAppointmentDate(appointment.getAppointmentDate())) {
            errors.add("Appointment date must be today or a future date");
        }

        // Validate appointment time
        if (!isValidAppointmentTime(appointment.getAppointmentTime())) {
            errors.add("Appointment time must be within clinic hours (08:00 - 20:00)");
        }

        return errors;
    }
}
