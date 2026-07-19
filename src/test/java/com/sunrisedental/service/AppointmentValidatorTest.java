package com.sunrisedental.service;

import com.sunrisedental.exception.InvalidAppointmentException;
import com.sunrisedental.model.Appointment;
import com.sunrisedental.model.Patient;
import com.sunrisedental.model.Treatment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD "RED" Phase — Test class for AppointmentValidator.
 * 
 * These tests were written BEFORE the AppointmentValidator implementation
 * to define the expected validation rules for appointment data.
 * 
 * Validation Rules Tested:
 *   - Patient name must not be null or empty
 *   - Contact number must match Sri Lankan format (10 digits starting with 0)
 *   - Appointment date must not be in the past
 *   - Appointment time must be within clinic working hours (08:00 - 20:00)
 *   - Dentist name must not be null or empty
 *   - Treatment must not be null
 *   - Full appointment validation combining all rules
 * 
 * Following the Red-Green-Refactor cycle:
 *   1. RED:   Write these tests first — they will FAIL (no implementation)
 *   2. GREEN: Write AppointmentValidator.java to make all tests PASS
 *   3. REFACTOR: Clean up code while ensuring tests still pass
 */
@DisplayName("AppointmentValidator — Input Validation Tests")
class AppointmentValidatorTest {

    private AppointmentValidator validator;
    private Patient validPatient;
    private Treatment validTreatment;

    @BeforeEach
    void setUp() {
        validator = new AppointmentValidator();

        validPatient = new Patient(1, "Nimal Jayawardena", "45 Temple Road, Kandy", "0712345678");
        validTreatment = new Treatment(1, "Teeth Whitening", 5000.00);
    }

    // ═══════════════════════════════════════════════════════════════
    //  PATIENT NAME VALIDATION
    // ═══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Patient Name Validation")
    class PatientNameTests {

        @Test
        @DisplayName("TC-V01: Should accept a valid patient name")
        void shouldAcceptValidPatientName() {
            assertTrue(validator.isValidPatientName("Kamal Perera"),
                    "A non-empty name should be valid");
        }

        @Test
        @DisplayName("TC-V02: Should reject null patient name")
        void shouldRejectNullPatientName() {
            assertFalse(validator.isValidPatientName(null),
                    "Null patient name should be invalid");
        }

        @Test
        @DisplayName("TC-V03: Should reject empty patient name")
        void shouldRejectEmptyPatientName() {
            assertFalse(validator.isValidPatientName(""),
                    "Empty string patient name should be invalid");
        }

        @Test
        @DisplayName("TC-V04: Should reject whitespace-only patient name")
        void shouldRejectWhitespaceOnlyPatientName() {
            assertFalse(validator.isValidPatientName("   "),
                    "Whitespace-only patient name should be invalid");
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  CONTACT NUMBER VALIDATION
    // ═══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Contact Number Validation")
    class ContactNumberTests {

        @Test
        @DisplayName("TC-V05: Should accept valid Sri Lankan mobile number (07XXXXXXXX)")
        void shouldAcceptValidMobileNumber() {
            assertTrue(validator.isValidContactNumber("0771234567"),
                    "Valid 10-digit number starting with 07 should be accepted");
        }

        @Test
        @DisplayName("TC-V06: Should accept valid Sri Lankan landline (0XXXXXXXXX)")
        void shouldAcceptValidLandlineNumber() {
            assertTrue(validator.isValidContactNumber("0112345678"),
                    "Valid 10-digit landline number should be accepted");
        }

        @Test
        @DisplayName("TC-V07: Should reject null contact number")
        void shouldRejectNullContactNumber() {
            assertFalse(validator.isValidContactNumber(null),
                    "Null contact number should be invalid");
        }

        @Test
        @DisplayName("TC-V08: Should reject contact number with less than 10 digits")
        void shouldRejectShortContactNumber() {
            assertFalse(validator.isValidContactNumber("077123"),
                    "Contact number with fewer than 10 digits should be invalid");
        }

        @Test
        @DisplayName("TC-V09: Should reject contact number with letters")
        void shouldRejectContactNumberWithLetters() {
            assertFalse(validator.isValidContactNumber("077ABC4567"),
                    "Contact number containing letters should be invalid");
        }

        @Test
        @DisplayName("TC-V10: Should reject contact number not starting with 0")
        void shouldRejectContactNotStartingWithZero() {
            assertFalse(validator.isValidContactNumber("1771234567"),
                    "Contact number not starting with 0 should be invalid");
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  APPOINTMENT DATE VALIDATION
    // ═══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Appointment Date Validation")
    class DateValidationTests {

        @Test
        @DisplayName("TC-V11: Should accept a future date")
        void shouldAcceptFutureDate() {
            LocalDate futureDate = LocalDate.now().plusDays(7);
            assertTrue(validator.isValidAppointmentDate(futureDate),
                    "A date 7 days in the future should be valid");
        }

        @Test
        @DisplayName("TC-V12: Should accept today's date")
        void shouldAcceptTodaysDate() {
            assertTrue(validator.isValidAppointmentDate(LocalDate.now()),
                    "Today's date should be valid for same-day appointments");
        }

        @Test
        @DisplayName("TC-V13: Should reject a past date")
        void shouldRejectPastDate() {
            LocalDate pastDate = LocalDate.now().minusDays(1);
            assertFalse(validator.isValidAppointmentDate(pastDate),
                    "Yesterday's date should be invalid — cannot book in the past");
        }

        @Test
        @DisplayName("TC-V14: Should reject null date")
        void shouldRejectNullDate() {
            assertFalse(validator.isValidAppointmentDate(null),
                    "Null date should be invalid");
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  APPOINTMENT TIME VALIDATION (Clinic hours: 08:00 - 20:00)
    // ═══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Appointment Time Validation (Clinic Hours: 08:00–20:00)")
    class TimeValidationTests {

        @Test
        @DisplayName("TC-V15: Should accept time within clinic hours (10:00)")
        void shouldAcceptTimeWithinClinicHours() {
            assertTrue(validator.isValidAppointmentTime(LocalTime.of(10, 0)),
                    "10:00 AM is within clinic hours");
        }

        @Test
        @DisplayName("TC-V16: Should accept clinic opening time (08:00)")
        void shouldAcceptOpeningTime() {
            assertTrue(validator.isValidAppointmentTime(LocalTime.of(8, 0)),
                    "08:00 AM (opening time) should be valid");
        }

        @Test
        @DisplayName("TC-V17: Should reject time before clinic opens (06:00)")
        void shouldRejectTimeBeforeOpening() {
            assertFalse(validator.isValidAppointmentTime(LocalTime.of(6, 0)),
                    "06:00 AM is before clinic opens");
        }

        @Test
        @DisplayName("TC-V18: Should reject time after clinic closes (21:00)")
        void shouldRejectTimeAfterClosing() {
            assertFalse(validator.isValidAppointmentTime(LocalTime.of(21, 0)),
                    "21:00 is after clinic closes");
        }

        @Test
        @DisplayName("TC-V19: Should reject null time")
        void shouldRejectNullTime() {
            assertFalse(validator.isValidAppointmentTime(null),
                    "Null time should be invalid");
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  FULL APPOINTMENT VALIDATION
    // ═══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Full Appointment Validation")
    class FullValidationTests {

        @Test
        @DisplayName("TC-V20: Should return empty error list for valid appointment")
        void shouldReturnNoErrorsForValidAppointment() {
            // Arrange: Fully valid appointment
            Appointment appointment = new Appointment(
                    "APT-20240720-001",
                    validPatient,
                    "Dr. Silva",
                    validTreatment,
                    LocalDate.now().plusDays(1),
                    LocalTime.of(10, 0)
            );

            // Act
            List<String> errors = validator.validateAppointment(appointment);

            // Assert
            assertTrue(errors.isEmpty(),
                    "A fully valid appointment should have no validation errors");
        }

        @Test
        @DisplayName("TC-V21: Should return multiple errors for completely invalid appointment")
        void shouldReturnMultipleErrorsForInvalidAppointment() {
            // Arrange: Appointment with multiple problems
            Patient badPatient = new Patient(0, "", "", "ABC");
            Appointment appointment = new Appointment(
                    "APT-20240720-002",
                    badPatient,
                    "",  // empty dentist name
                    null,  // no treatment
                    LocalDate.now().minusDays(5),  // past date
                    LocalTime.of(23, 0)  // outside clinic hours
            );

            // Act
            List<String> errors = validator.validateAppointment(appointment);

            // Assert: Should have at least 4 errors
            assertFalse(errors.isEmpty(), "Invalid appointment should have errors");
            assertTrue(errors.size() >= 4,
                    "Should detect multiple validation errors, found: " + errors.size());
        }

        @Test
        @DisplayName("TC-V22: Should throw exception for null appointment")
        void shouldThrowExceptionForNullAppointment() {
            assertThrows(InvalidAppointmentException.class,
                    () -> validator.validateAppointment(null),
                    "Should throw InvalidAppointmentException for null appointment");
        }

        @Test
        @DisplayName("TC-V23: Should detect missing dentist name")
        void shouldDetectMissingDentistName() {
            // Arrange
            Appointment appointment = new Appointment(
                    "APT-20240720-003",
                    validPatient,
                    null,  // missing dentist
                    validTreatment,
                    LocalDate.now().plusDays(1),
                    LocalTime.of(10, 0)
            );

            // Act
            List<String> errors = validator.validateAppointment(appointment);

            // Assert
            assertFalse(errors.isEmpty(), "Should have at least one error");
            assertTrue(errors.stream().anyMatch(e -> e.toLowerCase().contains("dentist")),
                    "Error list should mention missing dentist name");
        }
    }
}
