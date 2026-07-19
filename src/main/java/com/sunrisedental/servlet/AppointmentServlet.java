package com.sunrisedental.servlet;

import com.sunrisedental.exception.InvalidAppointmentException;
import com.sunrisedental.model.Appointment;
import com.sunrisedental.model.Patient;
import com.sunrisedental.model.Treatment;
import com.sunrisedental.repository.AppointmentDAO;
import com.sunrisedental.repository.AppointmentDAOImpl;
import com.sunrisedental.service.AppointmentService;
import com.sunrisedental.service.AppointmentValidator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Presentation Tier (Web) — Appointment Servlet.
 * 
 * Handles form submissions from register-appointment.jsp.
 * Maps incoming HTTP parameters to domain objects, invokes the Business
 * Logic Tier (AppointmentService) for server-side validation and persistence,
 * and handles routing/redirects based on success or failure.
 */
@WebServlet("/appointment")
public class AppointmentServlet extends HttpServlet {

    private AppointmentService appointmentService;

    @Override
    public void init() throws ServletException {
        // Initialize Business Logic components (In a real Spring app, we'd use Dependency Injection)
        AppointmentDAO dao = new AppointmentDAOImpl();
        AppointmentValidator validator = new AppointmentValidator();
        this.appointmentService = new AppointmentService(dao, validator);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Protect endpoint from unauthorized access
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("register".equals(action)) {
            handleRegistration(request, response);
        }
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // 1. Extract and map parameters to Domain Objects
            Patient patient = new Patient();
            patient.setPatientName(request.getParameter("patientName"));
            patient.setContactNumber(request.getParameter("contactNumber"));

            Treatment treatment = new Treatment();
            treatment.setTreatmentId(Integer.parseInt(request.getParameter("treatmentId")));

            Appointment appointment = new Appointment();
            appointment.setPatient(patient);
            appointment.setDentistName(request.getParameter("dentistName"));
            appointment.setTreatment(treatment);
            appointment.setAppointmentDate(LocalDate.parse(request.getParameter("date")));
            appointment.setAppointmentTime(LocalTime.parse(request.getParameter("time")));

            // 2. Invoke Business Logic Tier (Validates and checks for conflicts)
            Appointment savedAppt = appointmentService.registerAppointment(appointment);

            // 3. Success Routing
            request.setAttribute("message", "Success! Appointment Registered. Number: " + savedAppt.getAppointmentNumber());
            request.getRequestDispatcher("register-appointment.jsp").forward(request, response);

        } catch (InvalidAppointmentException e) {
            // Handle Business Rule / Validation Violations
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("register-appointment.jsp").forward(request, response);
            
        } catch (Exception e) {
            // Handle unexpected system errors (e.g., date parsing failure, DB issues)
            request.setAttribute("error", "System Error: " + e.getMessage());
            request.getRequestDispatcher("register-appointment.jsp").forward(request, response);
        }
    }
}
