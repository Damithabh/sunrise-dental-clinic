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
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

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

        // Fetch all appointments for dashboard display
        List<Appointment> recentAppointments = appointmentService.getAllAppointments();
        
        // Pass data to JSP
        request.setAttribute("recentAppointments", recentAppointments);
        request.setAttribute("appointmentsCount", recentAppointments.size());
        
        // Assuming some mock calculations for Revenue & Bills for now
        // In a real scenario, this would come from BillingService/DAO
        double totalRevenue = recentAppointments.size() * 3500.0;
        request.setAttribute("totalRevenue", totalRevenue);
        
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
