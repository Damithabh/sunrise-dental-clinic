package com.sunrisedental.repository;

import com.sunrisedental.config.DatabaseConnection;
import com.sunrisedental.model.Appointment;
import com.sunrisedental.model.Patient;
import com.sunrisedental.model.Treatment;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC Implementation of the AppointmentDAO interface.
 * 
 * Demonstrates the Data Access Object (DAO) pattern using standard JDBC and
 * the Singleton DatabaseConnection class to manage database connections.
 */
@Repository
public class AppointmentDAOImpl implements AppointmentDAO {

    @Override
    public boolean save(Appointment appointment) {
        String sql = "INSERT INTO appointments (appointment_number, patient_id, dentist_name, " +
                     "treatment_id, appointment_date, appointment_time, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
                     
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, appointment.getAppointmentNumber());
            // Assuming Patient and Treatment are already persisted and have IDs assigned.
            pstmt.setInt(2, appointment.getPatient().getPatientId());
            pstmt.setString(3, appointment.getDentistName());
            pstmt.setInt(4, appointment.getTreatment().getTreatmentId());
            pstmt.setDate(5, Date.valueOf(appointment.getAppointmentDate()));
            pstmt.setTime(6, Time.valueOf(appointment.getAppointmentTime()));
            pstmt.setString(7, appointment.getStatus());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error saving appointment: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Appointment> findByAppointmentNumber(String appointmentNumber) {
        String sql = "SELECT a.*, p.patient_name, p.contact_number, t.treatment_type, t.treatment_cost " +
                     "FROM appointments a " +
                     "JOIN patients p ON a.patient_id = p.patient_id " +
                     "JOIN treatments t ON a.treatment_id = t.treatment_id " +
                     "WHERE a.appointment_number = ?";
                     
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, appointmentNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToAppointment(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding appointment by number: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Appointment> findByDentistAndDate(String dentistName, LocalDate date) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, p.patient_name, p.contact_number, t.treatment_type, t.treatment_cost " +
                     "FROM appointments a " +
                     "JOIN patients p ON a.patient_id = p.patient_id " +
                     "JOIN treatments t ON a.treatment_id = t.treatment_id " +
                     "WHERE a.dentist_name = ? AND a.appointment_date = ?";
                     
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, dentistName);
            pstmt.setDate(2, Date.valueOf(date));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapRowToAppointment(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding appointments by dentist and date: " + e.getMessage());
        }
        return appointments;
    }

    @Override
    public List<Appointment> findAll() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, p.patient_name, p.contact_number, t.treatment_type, t.treatment_cost " +
                     "FROM appointments a " +
                     "JOIN patients p ON a.patient_id = p.patient_id " +
                     "JOIN treatments t ON a.treatment_id = t.treatment_id";
                     
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
             
            while (rs.next()) {
                appointments.add(mapRowToAppointment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all appointments: " + e.getMessage());
        }
        return appointments;
    }

    @Override
    public boolean update(Appointment appointment) {
        String sql = "UPDATE appointments SET dentist_name = ?, treatment_id = ?, " +
                     "appointment_date = ?, appointment_time = ?, status = ? " +
                     "WHERE appointment_number = ?";
                     
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, appointment.getDentistName());
            pstmt.setInt(2, appointment.getTreatment().getTreatmentId());
            pstmt.setDate(3, Date.valueOf(appointment.getAppointmentDate()));
            pstmt.setTime(4, Time.valueOf(appointment.getAppointmentTime()));
            pstmt.setString(5, appointment.getStatus());
            pstmt.setString(6, appointment.getAppointmentNumber());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating appointment: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String appointmentNumber) {
        String sql = "DELETE FROM appointments WHERE appointment_number = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, appointmentNumber);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting appointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to map a ResultSet row to an Appointment object.
     */
    private Appointment mapRowToAppointment(ResultSet rs) throws SQLException {
        // Hydrate Patient
        Patient patient = new Patient();
        patient.setPatientId(rs.getInt("patient_id"));
        patient.setPatientName(rs.getString("patient_name"));
        patient.setContactNumber(rs.getString("contact_number"));
        
        // Hydrate Treatment
        Treatment treatment = new Treatment();
        treatment.setTreatmentId(rs.getInt("treatment_id"));
        treatment.setTreatmentType(rs.getString("treatment_type"));
        treatment.setTreatmentCost(rs.getDouble("treatment_cost"));
        
        // Hydrate Appointment
        Appointment appointment = new Appointment(
            rs.getString("appointment_number"),
            patient,
            rs.getString("dentist_name"),
            treatment,
            rs.getDate("appointment_date").toLocalDate(),
            rs.getTime("appointment_time").toLocalTime()
        );
        appointment.setStatus(rs.getString("status"));
        
        return appointment;
    }
}
