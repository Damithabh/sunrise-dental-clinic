# Phase 3: Testing & TDD — Sunrise Dental Clinic
## Appointment & Patient Management System

---

## 1. TDD Rationale & Approach

### 1.1 Why Test-Driven Development?

The Sunrise Dental Clinic system follows the **Test-Driven Development (TDD)** methodology using the **Red-Green-Refactor** cycle. TDD was chosen for this project because:

1. **Patient data accuracy is critical** — Billing errors and double bookings were the core problems identified in the scenario. TDD ensures the business logic that prevents these issues is rigorously validated before deployment.
2. **The 3-tier architecture demands isolated testing** — The Business Logic Tier must function correctly independently of the Presentation Tier (web UI) and the Data Access Tier (database).
3. **Distributed applications require predictable behaviour** — Since the business layer is exposed as RESTful web services, TDD guarantees that each service method produces correct, predictable outputs regardless of how the frontend calls it.

### 1.2 The Red-Green-Refactor Cycle Applied

| Phase | Activity | System Application |
|-------|----------|--------------------|
| **RED** | Write comprehensive test classes that define expected behaviour. All tests fail because no implementation exists. | `BillingServiceTest` (13 tests) and `AppointmentValidatorTest` (23 tests) were written first, specifying exact inputs, outputs, and exception behaviour. |
| **GREEN** | Write the minimum implementation code to make every failing test pass. Focus on correctness, not elegance. | `BillingService.java` and `AppointmentValidator.java` were implemented method-by-method until all 36 tests turned green. |
| **REFACTOR** | With all tests passing, improve code clarity, remove duplication, and enhance documentation. Re-run all tests after every change. | Constants were extracted (e.g., `CONSULTATION_FEE = 500.00`), validation methods were decomposed into focused helper methods, and Javadoc was added throughout. |

### 1.3 Benefits Achieved Through TDD

- **Early defect detection** — A critical bug in the discount calculation (applying discount to treatment cost only, instead of the full subtotal) was caught during the GREEN phase before any UI existed.
- **Fearless refactoring** — When the `Bill.recalculateTotal()` method was refactored to handle edge cases, the existing 13 billing tests immediately confirmed no regression occurred.
- **Living documentation** — The 36 test cases serve as executable specifications. Any developer can read `shouldCalculateBillWithStandardTreatment()` and understand exactly how billing works without reading the implementation.
- **Reliable web services** — The REST API controller delegates entirely to `AppointmentService` and `BillingService`. Since these services are thoroughly tested, the API produces consistent results.

---

## 2. Test Plan

### 2.1 Test Plan Overview

| Field | Detail |
|-------|--------|
| **Project** | Sunrise Dental Clinic — Appointment & Patient Management System |
| **Test Level** | Unit Testing (Business Logic Tier) |
| **Testing Framework** | JUnit 5 (Jupiter) with Assertions |
| **Test Runner** | Maven Surefire Plugin 3.2.5 |
| **Coverage Tool** | JaCoCo 0.8.12 |
| **Build Tool** | Apache Maven 3.9.6 |
| **Java Version** | JDK 17 |
| **Approach** | Test-Driven Development (Red-Green-Refactor) |

### 2.2 Scope of Testing

| In Scope | Out of Scope (Phase 3) |
|----------|----------------------|
| `BillingService.calculateBill()` — all bill calculation scenarios | Controller (HTTP) layer testing |
| `BillingService.applyDiscount()` — discount validation and application | Database integration testing |
| `AppointmentValidator` — all field-level validators | UI/JSP template rendering |
| `AppointmentValidator.validateAppointment()` — full appointment validation | End-to-end browser testing |
| Domain model methods (`Bill.recalculateTotal()`, `Bill.generateReceipt()`) | Load/performance testing |
| Exception handling (`AppointmentNotFoundException`, `InvalidAppointmentException`) | Security/penetration testing |

### 2.3 Test Classes Summary

| Test Class | Test Methods | Focus Area |
|-----------|-------------|------------|
| `BillingServiceTest` | 13 tests | Bill calculation, discounts, error handling, receipt generation |
| `AppointmentValidatorTest` | 23 tests | Input validation for all appointment fields |
| `SunriseDentalApplicationTests` | 1 test | Spring Boot context loading verification |
| **Total** | **37 tests** | |

---

## 3. Test Data

### 3.1 Test Data for Bill Calculation (`BillingServiceTest`)

| Test Case ID | Description | Input Data | Expected Output |
|-------------|-------------|------------|-----------------|
| **TC-B01** | Standard bill: Consultation + Teeth Cleaning | Treatment: "Teeth Cleaning" @ LKR 3,000 | consultation=500, treatment=3000, **total=3500** |
| **TC-B02** | High-cost treatment: Root Canal | Treatment: "Root Canal" @ LKR 15,000 | consultation=500, treatment=15000, **total=15500** |
| **TC-B03** | Zero-cost treatment: Consultation Only | Treatment: "Consultation Only" @ LKR 0 | consultation=500, treatment=0, **total=500** |
| **TC-B04** | Bill references correct appointment | Appointment: APT-20240715-004 | `bill.getAppointment().getAppointmentNumber()` = "APT-20240715-004" |
| **TC-B05** | Bill date is today | Any valid appointment | `bill.getBillDate()` = `LocalDate.now()` |
| **TC-B06** | Apply 10% discount | Bill total = 3500, discount = 10% | **total = 3150** (3500 × 0.90) |
| **TC-B07** | Apply 0% discount (no change) | Bill total = 3500, discount = 0% | **total = 3500** (unchanged) |
| **TC-B08** | Reject negative discount | discount = -5% | Throws `IllegalArgumentException` |
| **TC-B09** | Reject discount > 100% | discount = 150% | Throws `IllegalArgumentException` |
| **TC-B10** | Null appointment | `null` | Throws `AppointmentNotFoundException` |
| **TC-B11** | Appointment with no treatment | `treatment = null` | Throws `IllegalArgumentException` |
| **TC-B12** | Appointment with null number | `appointmentNumber = null` | Throws `AppointmentNotFoundException` |
| **TC-B13** | Receipt contains key details | Patient: "Kamal Perera", Treatment: "Teeth Cleaning" | Receipt string contains patient name, treatment type, appointment number |

### 3.2 Test Data for Appointment Validation (`AppointmentValidatorTest`)

| Test Case ID | Description | Input Data | Expected Output |
|-------------|-------------|------------|-----------------|
| **TC-V01** | Valid patient name | `"Kamal Perera"` | `true` |
| **TC-V02** | Null patient name | `null` | `false` |
| **TC-V03** | Empty patient name | `""` | `false` |
| **TC-V04** | Whitespace-only name | `"   "` | `false` |
| **TC-V05** | Valid mobile number | `"0771234567"` | `true` |
| **TC-V06** | Valid landline number | `"0112345678"` | `true` |
| **TC-V07** | Null contact number | `null` | `false` |
| **TC-V08** | Short contact number | `"077123"` | `false` |
| **TC-V09** | Contact with letters | `"077ABC4567"` | `false` |
| **TC-V10** | Contact not starting with 0 | `"1771234567"` | `false` |
| **TC-V11** | Future date (valid) | `LocalDate.now().plusDays(7)` | `true` |
| **TC-V12** | Today's date (valid) | `LocalDate.now()` | `true` |
| **TC-V13** | Past date (yesterday) | `LocalDate.now().minusDays(1)` | `false` |
| **TC-V14** | Null date | `null` | `false` |
| **TC-V15** | Time within hours (10:00) | `LocalTime.of(10, 0)` | `true` |
| **TC-V16** | Clinic opening time (08:00) | `LocalTime.of(8, 0)` | `true` |
| **TC-V17** | Before opening (06:00) | `LocalTime.of(6, 0)` | `false` |
| **TC-V18** | After closing (21:00) | `LocalTime.of(21, 0)` | `false` |
| **TC-V19** | Null time | `null` | `false` |
| **TC-V20** | Fully valid appointment | All fields valid, future date, 10:00 | Empty error list `[]` |
| **TC-V21** | Completely invalid appointment | Empty name, bad contact, past date, 23:00, no treatment, no dentist | Error list with >= 4 errors |
| **TC-V22** | Null appointment object | `null` | Throws `InvalidAppointmentException` |
| **TC-V23** | Missing dentist name | Dentist name = `null`, all other fields valid | Error list contains "dentist" message |

---

## 4. Test Execution Results

### 4.1 Maven Test Execution

Tests are executed using the Maven Surefire Plugin with JaCoCo code coverage instrumentation:

```bash
# Command to run all tests
mvn test

# Command to run tests with coverage report
mvn test jacoco:report
```

### 4.2 Test Execution Output

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.sunrisedental.service.AppointmentValidatorTest
[INFO] Tests run: 23, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.sunrisedental.service.BillingServiceTest
[INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.sunrisedental.SunriseDentalApplicationTests
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 37, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] --- jacoco:0.8.12:report (report) @ sunrise-dental-clinic ---
[INFO] Analyzed bundle 'Sunrise Dental Clinic' with 26 classes
[INFO] BUILD SUCCESS
[INFO] Total time: 12.805 s
```

**Result: All 37 tests passed with zero failures and zero errors.**

*(Screenshot of test execution in the IDE/terminal should be inserted here in the final PDF submission.)*

### 4.3 JaCoCo Code Coverage Report

The JaCoCo coverage report (generated at `target/site/jacoco/index.html`) confirms comprehensive testing of the Business Logic Tier:

| Package | Class | Instruction Coverage | Branch Coverage |
|---------|-------|---------------------|----------------|
| `com.sunrisedental.service` | `BillingService` | High | High |
| `com.sunrisedental.service` | `AppointmentValidator` | High | High |
| `com.sunrisedental.model` | `Bill` | High | High |
| `com.sunrisedental.exception` | All exception classes | Covered | N/A |

*(Screenshot of the JaCoCo HTML report should be inserted here in the final PDF submission.)*

---

## 5. Requirement Traceability Matrix

This matrix maps each functional requirement from the case study to the test cases that verify it, demonstrating **full traceability** from requirements to tests.

| Requirement (from Brief) | Related Use Case | Test Cases | Status |
|--------------------------|-----------------|------------|--------|
| **R1:** Register new appointment with patient details | UC-03 | TC-V01 to TC-V04 (name validation), TC-V05 to TC-V10 (contact validation) | ✅ Verified |
| **R2:** Appointment date and time must be valid | UC-03 | TC-V11 to TC-V14 (date), TC-V15 to TC-V19 (time) | ✅ Verified |
| **R3:** Calculate total treatment cost | UC-05 | TC-B01 to TC-B03 (cost calculation) | ✅ Verified |
| **R4:** Calculate bill with consultation fee | UC-05 | TC-B01 to TC-B05 (consultation + treatment) | ✅ Verified |
| **R5:** Print patient bill/receipt | UC-05 | TC-B13 (receipt generation) | ✅ Verified |
| **R6:** Handle invalid/missing data gracefully | UC-03, UC-05 | TC-V20 to TC-V23 (validation), TC-B10 to TC-B12 (null handling) | ✅ Verified |
| **R7:** Discount application during billing | UC-13 | TC-B06 to TC-B09 (discount validation) | ✅ Verified |
| **R8:** Dentist must be assigned to appointment | UC-03 | TC-V23 (missing dentist) | ✅ Verified |
| **R9:** Treatment must be assigned before billing | UC-05 | TC-B11 (null treatment) | ✅ Verified |
| **R10:** System must reject completely invalid input | UC-03 | TC-V21 (all fields invalid) | ✅ Verified |

---

## 6. Running the Tests

### 6.1 Prerequisites

- **Java JDK 17+** installed and on your system PATH
- **Apache Maven 3.9+** installed (or use the included Maven wrapper)

### 6.2 Commands

```bash
# Run all tests
mvn test

# Run only BillingService tests
mvn test -Dtest=com.sunrisedental.service.BillingServiceTest

# Run only AppointmentValidator tests
mvn test -Dtest=com.sunrisedental.service.AppointmentValidatorTest

# Run tests with JaCoCo coverage report
mvn test jacoco:report

# View coverage report (open in browser)
# target/site/jacoco/index.html
```

---

## 7. Evaluation: Lessons Learned

### 7.1 Overall Assessment

The TDD approach was **highly effective** for this project. Writing tests before implementation forced a clear definition of what each method should do, reducing ambiguity and preventing scope creep during development.

### 7.2 What Worked Well

| Aspect | Observation |
|--------|-------------|
| **Boundary testing** | Writing test cases for boundary values (e.g., clinic opening time 08:00, closing time 20:00, past dates vs today's date) revealed edge cases that would have been overlooked in implementation-first development. |
| **Exception handling design** | TDD forced the creation of specific exception classes (`AppointmentNotFoundException`, `InvalidAppointmentException`) because the tests demanded specific exception types rather than generic `RuntimeException`. |
| **Discount validation** | The test for negative discount percentages (TC-B08) exposed the need for explicit validation that was not in the original design — without TDD, this edge case might have reached production. |
| **Bill receipt testing** | TC-B13 (testing receipt content) ensured that the `generateReceipt()` method includes all required patient and appointment details, acting as a specification for the receipt format. |

### 7.3 Challenges Encountered

| Challenge | Resolution |
|-----------|------------|
| Testing date-dependent logic (today's date changes daily) | Used `LocalDate.now()` in assertions to ensure tests remain valid regardless of when they are executed. |
| JaCoCo instrumentation warning with JDK 17+ | The `Unsupported class file major version 70` warning appears but does not affect test results. This is a known compatibility issue with JaCoCo 0.8.12 and newer JDK versions. |
| Isolating service tests from database | Services were designed with constructor injection, allowing mock DAOs to be passed during testing. The tests operate entirely in-memory without requiring a database. |

### 7.4 Recommendations for Future Testing

1. **Integration Testing** — Add tests that verify the interaction between the Service layer and the DAO layer using an in-memory H2 database.
2. **Controller Testing** — Use Spring MockMvc to test the REST API endpoints (`/api/appointments`) with HTTP request/response validation.
3. **UI Testing** — Implement Selenium WebDriver tests to automate browser-based testing of the JSP pages.
4. **Continuous Testing** — The GitHub Actions CI/CD pipeline already runs `mvn test` on every push, ensuring no regressions are introduced.

---

*End of Phase 3: Testing & TDD*
