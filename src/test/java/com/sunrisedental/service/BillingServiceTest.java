package com.sunrisedental.service;

import com.sunrisedental.exception.AppointmentNotFoundException;
import com.sunrisedental.model.Appointment;
import com.sunrisedental.model.Bill;
import com.sunrisedental.model.Patient;
import com.sunrisedental.model.Treatment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD "RED" Phase — Test class for BillingService.
 * 
 * These tests were written BEFORE the BillingService implementation
 * to define the expected behaviour of the billing system.
 * 
 * Test Strategy:
 *   - Unit tests for calculateBill() with valid appointments
 *   - Edge cases: zero-cost treatments, high-value treatments
 *   - Exception handling: null appointments, missing treatment data
 *   - Discount application and recalculation
 * 
 * Following the Red-Green-Refactor cycle:
 *   1. RED:   Write these tests first — they will FAIL (no implementation)
 *   2. GREEN: Write BillingService.java to make all tests PASS
 *   3. REFACTOR: Clean up code while ensuring tests still pass
 */
@DisplayName("BillingService — Bill Calculation Tests")
class BillingServiceTest {

    private BillingService billingService;
    private Patient testPatient;
    private Treatment testTreatment;

    /**
     * Set up common test fixtures before each test method.
     * Creates a fresh BillingService instance and test data objects.
     */
    @BeforeEach
    void setUp() {
        billingService = new BillingService();

        // Standard test patient
        testPatient = new Patient(1, "Kamal Perera", "123 Galle Road, Colombo 03", "0771234567");

        // Standard test treatment: Teeth Cleaning at LKR 3,000
        testTreatment = new Treatment(1, "Teeth Cleaning", 3000.00);
    }

    // ═══════════════════════════════════════════════════════════════
    //  SUCCESSFUL BILL CALCULATION TESTS
    // ═══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Successful Bill Calculations")
    class SuccessfulCalculations {

        @Test
        @DisplayName("TC-B01: Should calculate bill as Consultation Fee + Treatment Cost")
        void shouldCalculateBillWithConsultationAndTreatmentCost() {
            // Arrange: Create a valid appointment
            Appointment appointment = new Appointment(
                    "APT-20240715-001",
                    testPatient,
                    "Dr. Silva",
                    testTreatment,  // Teeth Cleaning: LKR 3,000
                    LocalDate.of(2024, 7, 15),
                    LocalTime.of(10, 0)
            );

            // Act: Calculate the bill
            Bill bill = billingService.calculateBill(appointment);

            // Assert: Total = Consultation Fee (500) + Treatment Cost (3000) = 3500
            assertNotNull(bill, "Bill should not be null");
            assertEquals(500.00, bill.getConsultationFee(), 0.01,
                    "Consultation fee should be LKR 500.00");
            assertEquals(3000.00, bill.getTreatmentCost(), 0.01,
                    "Treatment cost should match treatment type cost");
            assertEquals(3500.00, bill.getTotalAmount(), 0.01,
                    "Total should be consultation fee + treatment cost");
        }

        @Test
        @DisplayName("TC-B02: Should calculate bill for Root Canal treatment (LKR 15,000)")
        void shouldCalculateBillForRootCanal() {
            // Arrange: Root Canal is a high-cost treatment
            Treatment rootCanal = new Treatment(2, "Root Canal", 15000.00);
            Appointment appointment = new Appointment(
                    "APT-20240715-002",
                    testPatient,
                    "Dr. Fernando",
                    rootCanal,
                    LocalDate.of(2024, 7, 15),
                    LocalTime.of(14, 30)
            );

            // Act
            Bill bill = billingService.calculateBill(appointment);

            // Assert: Total = 500 + 15000 = 15500
            assertEquals(15500.00, bill.getTotalAmount(), 0.01,
                    "Total should be LKR 15,500 for Root Canal + consultation");
        }

        @Test
        @DisplayName("TC-B03: Should calculate bill for Consultation Only (LKR 0 treatment)")
        void shouldCalculateBillForConsultationOnly() {
            // Arrange: Consultation-only visit with zero treatment cost
            Treatment consultationOnly = new Treatment(3, "Consultation Only", 0.00);
            Appointment appointment = new Appointment(
                    "APT-20240715-003",
                    testPatient,
                    "Dr. Silva",
                    consultationOnly,
                    LocalDate.of(2024, 7, 15),
                    LocalTime.of(9, 0)
            );

            // Act
            Bill bill = billingService.calculateBill(appointment);

            // Assert: Total = 500 + 0 = 500 (consultation fee only)
            assertEquals(500.00, bill.getTotalAmount(), 0.01,
                    "Total should be consultation fee only when treatment cost is zero");
        }

        @Test
        @DisplayName("TC-B04: Bill should reference the correct appointment")
        void billShouldReferenceCorrectAppointment() {
            // Arrange
            Appointment appointment = new Appointment(
                    "APT-20240715-004",
                    testPatient,
                    "Dr. Silva",
                    testTreatment,
                    LocalDate.of(2024, 7, 15),
                    LocalTime.of(11, 0)
            );

            // Act
            Bill bill = billingService.calculateBill(appointment);

            // Assert: Bill should be linked to the correct appointment
            assertNotNull(bill.getAppointment(), "Bill must reference an appointment");
            assertEquals("APT-20240715-004", bill.getAppointment().getAppointmentNumber(),
                    "Bill should reference the correct appointment number");
        }

        @Test
        @DisplayName("TC-B05: Bill date should be today's date")
        void billDateShouldBeToday() {
            // Arrange
            Appointment appointment = new Appointment(
                    "APT-20240715-005",
                    testPatient,
                    "Dr. Silva",
                    testTreatment,
                    LocalDate.of(2024, 7, 15),
                    LocalTime.of(15, 0)
            );

            // Act
            Bill bill = billingService.calculateBill(appointment);

            // Assert
            assertEquals(LocalDate.now(), bill.getBillDate(),
                    "Bill date should be set to today's date");
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  DISCOUNT APPLICATION TESTS
    // ═══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Discount Application")
    class DiscountTests {

        @Test
        @DisplayName("TC-B06: Should apply 10% discount correctly")
        void shouldApplyTenPercentDiscount() {
            // Arrange: Bill with total of 3500 (500 + 3000)
            Appointment appointment = new Appointment(
                    "APT-20240715-006",
                    testPatient,
                    "Dr. Silva",
                    testTreatment,
                    LocalDate.of(2024, 7, 15),
                    LocalTime.of(10, 0)
            );
            Bill bill = billingService.calculateBill(appointment);

            // Act: Apply 10% discount
            Bill discountedBill = billingService.applyDiscount(bill, 10.0);

            // Assert: 3500 * 0.90 = 3150
            assertEquals(10.0, discountedBill.getDiscountPercentage(), 0.01,
                    "Discount percentage should be stored");
            assertEquals(3150.00, discountedBill.getTotalAmount(), 0.01,
                    "Total after 10% discount should be LKR 3,150");
        }

        @Test
        @DisplayName("TC-B07: Should handle 0% discount (no change)")
        void shouldHandleZeroDiscount() {
            // Arrange
            Appointment appointment = new Appointment(
                    "APT-20240715-007",
                    testPatient,
                    "Dr. Silva",
                    testTreatment,
                    LocalDate.of(2024, 7, 15),
                    LocalTime.of(10, 0)
            );
            Bill bill = billingService.calculateBill(appointment);

            // Act
            Bill sameBill = billingService.applyDiscount(bill, 0.0);

            // Assert: Total should remain unchanged
            assertEquals(3500.00, sameBill.getTotalAmount(), 0.01,
                    "Total should remain unchanged with 0% discount");
        }

        @Test
        @DisplayName("TC-B08: Should reject negative discount percentage")
        void shouldRejectNegativeDiscount() {
            // Arrange
            Appointment appointment = new Appointment(
                    "APT-20240715-008",
                    testPatient,
                    "Dr. Silva",
                    testTreatment,
                    LocalDate.of(2024, 7, 15),
                    LocalTime.of(10, 0)
            );
            Bill bill = billingService.calculateBill(appointment);

            // Act & Assert: Negative discount should throw exception
            assertThrows(IllegalArgumentException.class,
                    () -> billingService.applyDiscount(bill, -5.0),
                    "Should throw IllegalArgumentException for negative discount");
        }

        @Test
        @DisplayName("TC-B09: Should reject discount greater than 100%")
        void shouldRejectDiscountOverHundred() {
            // Arrange
            Appointment appointment = new Appointment(
                    "APT-20240715-009",
                    testPatient,
                    "Dr. Silva",
                    testTreatment,
                    LocalDate.of(2024, 7, 15),
                    LocalTime.of(10, 0)
            );
            Bill bill = billingService.calculateBill(appointment);

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> billingService.applyDiscount(bill, 150.0),
                    "Should throw IllegalArgumentException for discount > 100%");
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  EXCEPTION / ERROR HANDLING TESTS
    // ═══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Error Handling & Edge Cases")
    class ErrorHandlingTests {

        @Test
        @DisplayName("TC-B10: Should throw exception for null appointment")
        void shouldThrowExceptionForNullAppointment() {
            // Act & Assert: Passing null should throw AppointmentNotFoundException
            assertThrows(AppointmentNotFoundException.class,
                    () -> billingService.calculateBill(null),
                    "Should throw AppointmentNotFoundException for null appointment");
        }

        @Test
        @DisplayName("TC-B11: Should throw exception when appointment has no treatment")
        void shouldThrowExceptionWhenNoTreatment() {
            // Arrange: Appointment without treatment
            Appointment appointment = new Appointment(
                    "APT-20240715-011",
                    testPatient,
                    "Dr. Silva",
                    null,  // No treatment assigned
                    LocalDate.of(2024, 7, 15),
                    LocalTime.of(10, 0)
            );

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> billingService.calculateBill(appointment),
                    "Should throw IllegalArgumentException when treatment is missing");
        }

        @Test
        @DisplayName("TC-B12: Should throw exception when appointment number is null")
        void shouldThrowExceptionWhenAppointmentNumberIsNull() {
            // Arrange: Appointment with null appointment number
            Appointment appointment = new Appointment(
                    null,  // No appointment number
                    testPatient,
                    "Dr. Silva",
                    testTreatment,
                    LocalDate.of(2024, 7, 15),
                    LocalTime.of(10, 0)
            );

            // Act & Assert
            assertThrows(AppointmentNotFoundException.class,
                    () -> billingService.calculateBill(appointment),
                    "Should throw AppointmentNotFoundException when appointment number is null");
        }

        @Test
        @DisplayName("TC-B13: Bill receipt should contain patient and treatment details")
        void billReceiptShouldContainDetails() {
            // Arrange
            Appointment appointment = new Appointment(
                    "APT-20240715-013",
                    testPatient,
                    "Dr. Silva",
                    testTreatment,
                    LocalDate.of(2024, 7, 15),
                    LocalTime.of(10, 0)
            );

            // Act
            Bill bill = billingService.calculateBill(appointment);
            String receipt = bill.generateReceipt();

            // Assert: Receipt should contain key information
            assertNotNull(receipt, "Receipt should not be null");
            assertTrue(receipt.contains("Kamal Perera"), "Receipt should contain patient name");
            assertTrue(receipt.contains("Teeth Cleaning"), "Receipt should contain treatment type");
            assertTrue(receipt.contains("APT-20240715-013"), "Receipt should contain appointment number");
        }
    }
}
