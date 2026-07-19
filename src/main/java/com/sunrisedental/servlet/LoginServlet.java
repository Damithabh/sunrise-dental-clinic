package com.sunrisedental.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Presentation Tier (Web) — Login Servlet.
 * 
 * Handles POST requests from the login.jsp form.
 * Implements strict HTTP session management to ensure secure access to the dashboard.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("logout".equals(action)) {
            // Handle Logout: Invalidate the session
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect("login.jsp");
            return;
        }

        // Handle Login
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Mock authentication for the assignment (admin/admin123)
        // In reality, this would query the Data Access Tier (users table)
        boolean isAuthenticated = "admin".equals(username) && "admin123".equals(password);

        if (isAuthenticated) {
            // Create a new session or return the current one
            HttpSession session = request.getSession(true);
            
            // Set session attributes for use across the web app
            session.setAttribute("username", username);
            session.setAttribute("role", "ADMIN");
            
            // Set session timeout to 30 minutes
            session.setMaxInactiveInterval(30 * 60);

            // Redirect to the main dashboard
            response.sendRedirect("dashboard.jsp");
        } else {
            // Authentication failed, return to login with error message
            request.setAttribute("error", "Invalid username or password. Please try again.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // GET requests to /login should just show the login page
        response.sendRedirect("login.jsp");
    }
}
