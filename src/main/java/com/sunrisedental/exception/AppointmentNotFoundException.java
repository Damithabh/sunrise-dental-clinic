package com.sunrisedental.exception;

/**
 * Custom exception thrown when an appointment cannot be found
 * by its appointment number in the system.
 * 
 * This is used by BillingService and AppointmentService to signal
 * that an operation was attempted on a non-existent appointment.
 */
public class AppointmentNotFoundException extends RuntimeException {

    private final String appointmentNumber;

    public AppointmentNotFoundException(String appointmentNumber) {
        super("Appointment not found with number: " + appointmentNumber);
        this.appointmentNumber = appointmentNumber;
    }

    public AppointmentNotFoundException(String appointmentNumber, Throwable cause) {
        super("Appointment not found with number: " + appointmentNumber, cause);
        this.appointmentNumber = appointmentNumber;
    }

    public String getAppointmentNumber() {
        return appointmentNumber;
    }
}
