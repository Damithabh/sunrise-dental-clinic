package com.sunrisedental.repository;

import com.sunrisedental.model.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) Interface for the Appointment entity.
 * 
 * Defines the standard CRUD operations and custom query methods for accessing
 * appointment data in the Data Access Tier. This abstraction ensures the Business
 * Logic Tier is decoupled from the underlying database implementation.
 * 
 * Design Pattern: DAO (Data Access Object)
 */
public interface AppointmentDAO {

    /**
     * Saves a new appointment to the database.
     * 
     * @param appointment the appointment to save
     * @return true if successful, false otherwise
     */
    boolean save(Appointment appointment);

    /**
     * Retrieves an appointment by its unique appointment number.
     * 
     * @param appointmentNumber the appointment number (e.g., APT-20240715-001)
     * @return an Optional containing the appointment if found, otherwise empty
     */
    Optional<Appointment> findByAppointmentNumber(String appointmentNumber);

    /**
     * Retrieves all appointments for a specific dentist on a specific date.
     * Useful for checking scheduling conflicts.
     * 
     * @param dentistName the name of the dentist
     * @param date the date of the appointments
     * @return a list of appointments
     */
    List<Appointment> findByDentistAndDate(String dentistName, LocalDate date);

    /**
     * Retrieves all scheduled appointments in the system.
     * 
     * @return a list of all appointments
     */
    List<Appointment> findAll();

    /**
     * Updates an existing appointment in the database.
     * 
     * @param appointment the appointment to update
     * @return true if successful, false otherwise
     */
    boolean update(Appointment appointment);

    /**
     * Deletes an appointment from the database by its appointment number.
     * 
     * @param appointmentNumber the appointment number
     * @return true if successful, false otherwise
     */
    boolean delete(String appointmentNumber);
}
