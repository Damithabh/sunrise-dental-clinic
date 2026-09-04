package com.sunrisedental.servlet;

import com.sunrisedental.model.Appointment;
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
import java.util.Optional;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    private AppointmentService appointmentService;

    @Override
    public void init() throws ServletException {
        AppointmentDAO dao = new AppointmentDAOImpl();
        AppointmentValidator validator = new AppointmentValidator();
        this.appointmentService = new AppointmentService(dao, validator);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String appointmentNumber = request.getParameter("appointmentNumber");
        if (appointmentNumber != null && !appointmentNumber.trim().isEmpty()) {
            Optional<Appointment> appointment = appointmentService.searchByAppointmentNumber(appointmentNumber.trim());
            if (appointment.isPresent()) {
                request.setAttribute("appointment", appointment.get());
            } else {
                request.setAttribute("error", "No appointment found with number: " + appointmentNumber);
            }
        } else {
            request.setAttribute("error", "Please provide a valid appointment number.");
        }

        request.getRequestDispatcher("search-result.jsp").forward(request, response);
    }
}
