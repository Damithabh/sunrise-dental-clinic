package com.sunrisedental.controller;

import com.sunrisedental.exception.InvalidAppointmentException;
import com.sunrisedental.model.Appointment;
import com.sunrisedental.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * AppointmentController — Presentation Tier (Distributed Web Services).
 * 
 * Exposes the Business Logic Tier via RESTful APIs. This controller acts as
 * the bridge connecting incoming HTTP requests (from frontend or other clients)
 * to the AppointmentService.
 * 
 * Base URL: /api/appointments
 */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * Constructor injection for AppointmentService dependency.
     */
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * POST /api/appointments
     * Registers a new appointment in the system.
     * 
     * @param appointment JSON payload representing the appointment
     * @return 201 Created with saved appointment, or 400 Bad Request if validation fails
     */
    @PostMapping
    public ResponseEntity<?> registerAppointment(@RequestBody Appointment appointment) {
        try {
            Appointment savedAppointment = appointmentService.registerAppointment(appointment);
            return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
            
        } catch (InvalidAppointmentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Validation Failed");
            errorResponse.put("messages", e.getValidationErrors());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Internal Server Error");
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /api/appointments/{appointmentNumber}
     * Retrieves an appointment's details by its unique appointment number.
     * 
     * @param appointmentNumber path variable (e.g., APT-20240715-XXXX)
     * @return 200 OK with appointment, or 404 Not Found
     */
    @GetMapping("/{appointmentNumber}")
    public ResponseEntity<?> getAppointmentDetails(@PathVariable String appointmentNumber) {
        Optional<Appointment> appointment = appointmentService.searchByAppointmentNumber(appointmentNumber);
        
        if (appointment.isPresent()) {
            return new ResponseEntity<>(appointment.get(), HttpStatus.OK);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Appointment not found for number: " + appointmentNumber);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
