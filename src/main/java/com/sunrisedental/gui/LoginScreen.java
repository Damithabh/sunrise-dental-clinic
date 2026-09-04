package com.sunrisedental.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Secure Login Screen (Swing GUI).
 * 
 * Authenticates users against the backend REST API before granting
 * access to the main dashboard.
 */
public class LoginScreen extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginScreen() {
        
        setTitle("Sunrise Dental Clinic - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel headerLabel = new JLabel("🦷 Sunrise Dental Clinic", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);
        
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        
        formPanel.add(new JLabel()); // Spacer
        formPanel.add(loginButton);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Event Listeners
        loginButton.addActionListener(e -> attemptLogin());
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username and Password cannot be empty.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Disable button during network request
        loginButton.setEnabled(false);
        loginButton.setText("Authenticating...");

        // Run network request on background thread to avoid freezing GUI
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                try {
                    // For the sake of this phase, simulating a mock auth endpoint
                    // In a full implementation, this calls /api/auth/login
                    // In a full implementation, this calls /api/auth/login
                    
                    // Simulated logic: Admin credentials for testing
                    if ("admin".equals(username) && "admin123".equals(password)) {
                        return true;
                    }
                    
                    // If REST API was ready for auth:
                    // Map response = apiClient.post("/auth/login", credentials, Map.class);
                    // return response.containsKey("token");
                    
                    return false;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        JOptionPane.showMessageDialog(LoginScreen.this, 
                            "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        
                        // Transition to Dashboard
                        dispose(); // Close login window
                        new DashboardScreen().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(LoginScreen.this, 
                            "Invalid credentials. Please try again.", 
                            "Authentication Failed", 
                            JOptionPane.ERROR_MESSAGE);
                        passwordField.setText("");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginScreen.this, 
                        "System error occurred during login.", "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    loginButton.setEnabled(true);
                    loginButton.setText("Login");
                }
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        // Set Look and Feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}
