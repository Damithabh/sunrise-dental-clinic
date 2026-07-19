# Phase 3: Testing & TDD — Sunrise Dental Clinic

---

## 1. TDD Rationale & Approach (~200 words)

### How TDD (Red-Green-Refactor) is Applied

The Sunrise Dental Clinic system follows the **Test-Driven Development (TDD)** methodology using the **Red-Green-Refactor** cycle:

1. **RED Phase** — We first wrote comprehensive JUnit 5 test classes (`BillingServiceTest` and `AppointmentValidatorTest`) that define the *expected behaviour* of the billing and validation logic. At this stage, no implementation existed, so all tests would fail (Red).

2. **GREEN Phase** — We then wrote the minimum implementation code in `BillingService.java` and `AppointmentValidator.java` to make every failing test pass. The focus was on correctness, not elegance — simply satisfying the test assertions.

3. **REFACTOR Phase** — With all tests green, we refactored the implementation for clarity, removed duplication, and improved documentation while continuously re-running tests to ensure no regression.

### Why TDD is Beneficial for a Distributed Application

TDD is particularly valuable for the Sunrise Dental Clinic system because it is a **distributed, 3-tier application** where the Business Logic Tier operates independently of the Presentation Tier (web UI) and Data Access Tier (database). By testing services in isolation *before* integrating with web controllers or JPA repositories, we achieve:

- **Early defect detection** — Business logic errors (e.g., incorrect bill calculations) are caught before they propagate to the UI or database, reducing debugging cost.
- **Reliable web services** — Since the business layer will be exposed as RESTful web services, TDD guarantees that each service method produces correct, predictable outputs regardless of how the frontend calls it.
- **Fearless refactoring** — As the system evolves (e.g., adding new treatment types or discount rules), the existing test suite acts as a safety net, immediately flagging any regressions.
- **Living documentation** — Test cases serve as executable specifications, documenting *exactly* how `calculateBill()` or `validateAppointment()` should behave for any future developer.

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
| **Approach** | Test-Driven Development (Red-Green-Refactor) |

### 2.2 Scope of Testing

| In Scope | Out of Scope (Phase 3) |
|----------|----------------------|
| `BillingService.calculateBill()` | Controller (HTTP) layer testing |
| `BillingService.applyDiscount()` | Database integration testing |
| `AppointmentValidator` — all field validators | UI/Thymeleaf template rendering |
| `AppointmentValidator.validateAppointment()` | End-to-end browser testing |
| Domain model (`Bill.recalculateTotal()`, `Bill.generateReceipt()`) | Load/performance testing |
| Exception handling (`AppointmentNotFoundException`, `InvalidAppointmentException`) | Security/penetration testing |

### 2.3 Test Classes Summary

| Test Class | Methods | Focus Area |
|-----------|---------|------------|
| `BillingServiceTest` | 13 tests | Bill calculation, discounts, error handling |
| `AppointmentValidatorTest` | 23 tests | Input validation for all appointment fields |
| **Total** | **36 tests** | |

---

## 3. Test Data

### 3.1 Test Data for Bill Calculation (`BillingServiceTest`)

| Test Case ID | Description | Input Data | Expected Output |
|-------------|-------------|------------|-----------------|
| **TC-B01** | Standard bill: Consultation + Teeth Cleaning | Appointment: APT-20240715-001, Treatment: "Teeth Cleaning" @ LKR 3,000 | Bill: consultation=500, treatment=3000, **total=3500** |
| **TC-B02** | High-cost treatment: Root Canal | Appointment: APT-20240715-002, Treatment: "Root Canal" @ LKR 15,000 | Bill: consultation=500, treatment=15000, **total=15500** |
| **TC-B03** | Zero-cost treatment: Consultation Only | Appointment: APT-20240715-003, Treatment: "Consultation Only" @ LKR 0 | Bill: consultation=500, treatment=0, **total=500** |
| **TC-B04** | Bill references correct appointment | Appointment: APT-20240715-004 | `bill.getAppointment().getAppointmentNumber()` = "APT-20240715-004" |
| **TC-B05** | Bill date is today | Any valid appointment | `bill.getBillDate()` = `LocalDate.now()` |
| **TC-B06** | Apply 10% discount | Bill total = 3500, discount = 10% | **total = 3150** (3500 x 0.90) |
| **TC-B07** | Apply 0% discount (no change) | Bill total = 3500, discount = 0% | **total = 3500** (unchanged) |
| **TC-B08** | Reject negative discount | Bill total = 3500, discount = -5% | Throws `IllegalArgumentException` |
| **TC-B09** | Reject discount > 100% | Bill total = 3500, discount = 150% | Throws `IllegalArgumentException` |
| **TC-B10** | Null appointment | `null` | Throws `AppointmentNotFoundException` |
| **TC-B11** | Appointment with no treatment | Appointment with `treatment = null` | Throws `IllegalArgumentException` |
| **TC-B12** | Appointment with null number | Appointment with `appointmentNumber = null` | Throws `AppointmentNotFoundException` |
| **TC-B13** | Receipt contains key details | Valid appointment for "Kamal Perera", "Teeth Cleaning" | Receipt string contains "Kamal Perera", "Teeth Cleaning", appointment number |

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

## 4. Running the Tests

Requires **Java 17+** and **Maven 3.9+** installed and on your system PATH.

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

*End of Phase 3: Testing & TDD*
