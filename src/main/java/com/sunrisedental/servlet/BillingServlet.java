package com.sunrisedental.servlet;

import com.sunrisedental.model.Appointment;
import com.sunrisedental.model.Bill;
import com.sunrisedental.repository.AppointmentDAO;
import com.sunrisedental.repository.AppointmentDAOImpl;
import com.sunrisedental.service.AppointmentService;
import com.sunrisedental.service.AppointmentValidator;
import com.sunrisedental.service.BillingService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/billing")
public class BillingServlet extends HttpServlet {

    private AppointmentService appointmentService;
    private BillingService billingService;

    @Override
    public void init() throws ServletException {
        AppointmentDAO dao = new AppointmentDAOImpl();
        AppointmentValidator validator = new AppointmentValidator();
        this.appointmentService = new AppointmentService(dao, validator);
        this.billingService = new BillingService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Show empty billing page
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        request.getRequestDispatcher("billing.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String appointmentNumber = request.getParameter("appointmentNumber");
        
        if (appointmentNumber != null && !appointmentNumber.trim().isEmpty()) {
            Optional<Appointment> appointmentOpt = appointmentService.searchByAppointmentNumber(appointmentNumber.trim());
            
            if (appointmentOpt.isPresent()) {
                try {
                    Bill bill = billingService.calculateBill(appointmentOpt.get());
                    request.setAttribute("bill", bill);
                } catch (Exception e) {
                    request.setAttribute("error", "Error calculating bill: " + e.getMessage());
                }
            } else {
                request.setAttribute("error", "No appointment found with number: " + appointmentNumber);
            }
        } else {
            request.setAttribute("error", "Please provide a valid appointment number.");
        }

        request.getRequestDispatcher("billing.jsp").forward(request, response);
    }
}
