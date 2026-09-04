package com.sunrisedental.service;

import com.sunrisedental.exception.InvalidAppointmentException;
import com.sunrisedental.model.Appointment;
import com.sunrisedental.repository.AppointmentDAO;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * AppointmentService — Business Logic Tier.
 * 
 * Orchestrates appointment-related business rules. It bridges the Presentation Tier 
 * (REST Controllers) and Data Access Tier (DAOs). It delegates validation to 
 * AppointmentValidator and persistence to AppointmentDAO.
 * 
 * Responsibilities:
 *   - Registering new appointments
 *   - Validating incoming appointment data
 *   - Checking for scheduling conflicts (double bookings)
 *   - Generating unique appointment numbers
 *   - Retrieving appointment details
 */
@Service
public class AppointmentService {

    private final AppointmentDAO appointmentDAO;
    private final AppointmentValidator validator;

    /**
     * Constructor injection for dependencies.
     */
    public AppointmentService(AppointmentDAO appointmentDAO, AppointmentValidator validator) {
        this.appointmentDAO = appointmentDAO;
        this.validator = validator;
    }

    /**
     * Registers a new appointment after performing validation and conflict checks.
     * 
     * @param appointment the appointment to register
     * @return the saved appointment with a generated appointment number
     * @throws InvalidAppointmentException if validation fails or a conflict is detected
     */
    public Appointment registerAppointment(Appointment appointment) {
        // Step 1: Validate input data
        List<String> validationErrors = validator.validateAppointment(appointment);
        if (!validationErrors.isEmpty()) {
            throw new InvalidAppointmentException(validationErrors);
        }

        // Step 2: Check for double booking conflict
        boolean hasConflict = checkForConflicts(appointment);
        if (hasConflict) {
            throw new InvalidAppointmentException("Time slot is already booked for Dr. " + appointment.getDentistName());
        }

        // Step 3: Generate a unique appointment number
        String appointmentNumber = generateAppointmentNumber(appointment);
        appointment.setAppointmentNumber(appointmentNumber);
        appointment.setStatus("SCHEDULED");

        // Step 4: Persist via DAO
        boolean isSaved = appointmentDAO.save(appointment);
        if (!isSaved) {
            throw new RuntimeException("Failed to save appointment to the database.");
        }

        return appointment;
    }

    /**
     * Retrieves an appointment by its unique appointment number.
     * 
     * @param appointmentNumber the appointment number
     * @return an Optional containing the appointment if found
     */
    public Optional<Appointment> searchByAppointmentNumber(String appointmentNumber) {
        if (appointmentNumber == null || appointmentNumber.trim().isEmpty()) {
            return Optional.empty();
        }
        return appointmentDAO.findByAppointmentNumber(appointmentNumber);
    }

    /**
     * Retrieves all scheduled appointments in the system.
     * 
     * @return a list of all appointments
     */
    public List<Appointment> getAllAppointments() {
        return appointmentDAO.findAll();
    }

    /**
     * Checks if a dentist already has an appointment booked at the same time on the same date.
     * 
     * @param appointment the proposed appointment
     * @return true if there is a conflict, false otherwise
     */
    public boolean checkForConflicts(Appointment appointment) {
        List<Appointment> existingAppointments = appointmentDAO.findByDentistAndDate(
                appointment.getDentistName(), 
                appointment.getAppointmentDate()
        );

        for (Appointment existing : existingAppointments) {
            // A simple conflict check: exact same time
            // In a real system, we'd check if time overlaps based on treatment duration
            if (existing.getAppointmentTime().equals(appointment.getAppointmentTime())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates a unique appointment number (e.g., APT-20240715-XXXX).
     */
    private String generateAppointmentNumber(Appointment appointment) {
        String datePart = appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // Using a short UUID segment for uniqueness
        String uniqueSegment = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "APT-" + datePart + "-" + uniqueSegment;
    }
}
