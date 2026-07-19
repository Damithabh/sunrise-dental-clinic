package com.sunrisedental.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Menu-Driven Dashboard Screen (Swing GUI).
 * 
 * Displays the main system menu after a successful login.
 */
public class DashboardScreen extends JFrame {

    public DashboardScreen() {
        setTitle("Sunrise Dental Clinic - Main Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel headerLabel = new JLabel("Sunrise Dental Clinic System");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Menu Buttons Panel
        JPanel menuPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JButton btnRegister = createMenuButton("📝 Register Appointment");
        JButton btnDisplay = createMenuButton("🔍 Display Details");
        JButton btnBill = createMenuButton("💰 Calculate & Print Bill");
        JButton btnHelp = createMenuButton("❓ Help Section");
        JButton btnExit = createMenuButton("🚪 Exit System");

        menuPanel.add(btnRegister);
        menuPanel.add(btnDisplay);
        menuPanel.add(btnBill);
        menuPanel.add(btnHelp);
        menuPanel.add(btnExit);

        mainPanel.add(menuPanel, BorderLayout.CENTER);
        
        // Footer
        JLabel footerLabel = new JLabel("Logged in as: Administrator", SwingConstants.CENTER);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        add(mainPanel);

        // Event Listeners
        btnRegister.addActionListener(e -> {
            new RegisterAppointmentScreen(this).setVisible(true);
            this.setVisible(false);
        });
        
        btnDisplay.addActionListener(e -> JOptionPane.showMessageDialog(this, "Display feature coming soon."));
        btnBill.addActionListener(e -> JOptionPane.showMessageDialog(this, "Billing feature coming soon."));
        btnHelp.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "Sunrise Dental Clinic Help:\n\n" +
            "1. Use 'Register Appointment' to book patients.\n" +
            "2. Use 'Calculate Bill' to finalize appointments.\n" +
            "For technical support, contact IT.", "Help Section", JOptionPane.INFORMATION_MESSAGE));
            
        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        return button;
    }
}
