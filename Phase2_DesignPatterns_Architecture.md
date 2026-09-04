# Phase 2: Design Patterns & Architecture Development — Sunrise Dental Clinic
## Appointment & Patient Management System

---

## 1. Design Pattern Identification & Evaluation

This section identifies, describes, and critically evaluates the design patterns applied in the development of the Sunrise Dental Clinic system. Each pattern is justified with code evidence demonstrating its implementation within the system's 3-tier architecture.

### 1.1 Singleton Pattern — `DatabaseConnection`

**Category:** Creational Pattern

**Description:** The Singleton pattern restricts the instantiation of a class to exactly one object and provides a global point of access to that instance. In the context of database-driven applications, this prevents the overhead and resource exhaustion caused by repeatedly creating and destroying database connections.

**Implementation in the System:**

The `DatabaseConnection` class implements the Singleton pattern to manage the JDBC connection to the MySQL database. It uses lazy initialization with thread-safe synchronization to ensure that only one connection instance is shared across the entire Data Access Tier.

```java
// File: com.sunrisedental.config.DatabaseConnection
@Component
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    // Private constructor prevents external instantiation
    private DatabaseConnection() { }

    // Thread-safe global access point
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Lazy connection establishment
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }
}
```

**Critical Evaluation:**

| Aspect | Analysis |
|--------|----------|
| **Advantage** | Eliminates redundant connection creation across all DAO classes, reducing memory usage and database server load. Each DAO (`AppointmentDAOImpl`, `PatientDAO`, etc.) calls `DatabaseConnection.getInstance().getConnection()` instead of creating its own connection. |
| **Advantage** | Provides centralised connection configuration — changing the database URL, driver, or credentials requires modification in only one class. |
| **Limitation** | The classic Singleton can become a bottleneck in high-concurrency scenarios. A production system would benefit from a connection pool (e.g., HikariCP) instead of a single shared connection. |
| **Justification** | For the scope of this dental clinic system — a single-location practice with limited concurrent users — the Singleton pattern provides sufficient performance while demonstrating the design principle clearly. |

---

### 1.2 Data Access Object (DAO) Pattern — `AppointmentDAO` / `AppointmentDAOImpl`

**Category:** Structural / Architectural Pattern

**Description:** The DAO pattern provides an abstract interface to the database, separating the data persistence logic from the business logic. This creates a clean boundary between the Business Logic Tier and the Data Access Tier, allowing either to change independently.

**Implementation in the System:**

The system defines an `AppointmentDAO` interface specifying the contract for all appointment-related database operations. The concrete implementation `AppointmentDAOImpl` contains the actual JDBC SQL queries.

```java
// File: com.sunrisedental.repository.AppointmentDAO (Interface)
public interface AppointmentDAO {
    boolean save(Appointment appointment);
    Optional<Appointment> findByAppointmentNumber(String appointmentNumber);
    List<Appointment> findByDentistAndDate(String dentistName, LocalDate date);
    List<Appointment> findAll();
    boolean update(Appointment appointment);
    boolean delete(String appointmentNumber);
}
```

```java
// File: com.sunrisedental.repository.AppointmentDAOImpl (Concrete)
@Repository
public class AppointmentDAOImpl implements AppointmentDAO {

    @Override
    public boolean save(Appointment appointment) {
        String sql = "INSERT INTO appointments (appointment_number, patient_id, " +
                     "dentist_name, treatment_id, appointment_date, " +
                     "appointment_time, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, appointment.getAppointmentNumber());
            pstmt.setInt(2, appointment.getPatient().getPatientId());
            pstmt.setString(3, appointment.getDentistName());
            // ... additional parameter bindings
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving appointment: " + e.getMessage());
            return false;
        }
    }
    // ... other method implementations
}
```

**Critical Evaluation:**

| Aspect | Analysis |
|--------|----------|
| **Advantage** | The `AppointmentService` (Business Logic Tier) depends only on the `AppointmentDAO` interface, not on the JDBC implementation. This means the data source can be switched from MySQL to PostgreSQL or even a file-based system without modifying any business logic code. |
| **Advantage** | Facilitates unit testing — the service layer can be tested using mock DAO implementations, isolating business logic from database dependencies. This is demonstrated in `AppointmentValidatorTest` and `BillingServiceTest`. |
| **Advantage** | Uses `PreparedStatement` for parameterised queries, preventing SQL injection attacks — a critical security concern in any web application handling personal health data. |
| **Limitation** | Each domain entity requires its own DAO interface and implementation, which increases the number of classes. For a larger system, an ORM framework like Hibernate (via Spring Data JPA) could reduce boilerplate. |
| **Justification** | The DAO pattern is the industry-standard approach for separating persistence concerns. For this system, it clearly demonstrates the Data Access Tier's independence from business rules. |

---

### 1.3 Model-View-Controller (MVC) Pattern

**Category:** Architectural Pattern

**Description:** MVC separates an application into three interconnected components: **Model** (data and business logic), **View** (user interface), and **Controller** (handles user input and coordinates between Model and View). This separation allows each component to be developed, tested, and modified independently.

**Implementation in the System:**

| MVC Component | System Implementation | Examples |
|---------------|----------------------|----------|
| **Model** | Domain entities in `com.sunrisedental.model` | `Patient`, `Appointment`, `Treatment`, `Bill` |
| **View** | JSP pages in `src/main/webapp/` | `login.jsp`, `dashboard.jsp`, `billing.jsp`, `register-appointment.jsp` |
| **Controller** | Servlets and REST controllers | `LoginServlet`, `DashboardServlet`, `AppointmentController` |

```java
// File: com.sunrisedental.servlet.DashboardServlet (Controller)
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Check session (security concern)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Set model data as request attributes
        request.setAttribute("username", session.getAttribute("username"));
        request.setAttribute("todayDate", java.time.LocalDate.now().toString());

        // 3. Forward to the View (JSP)
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
```

**Critical Evaluation:**

| Aspect | Analysis |
|--------|----------|
| **Advantage** | Clear separation of concerns — JSP pages contain only presentation markup and JSTL tags, while servlets handle request routing and business logic delegation. This makes the codebase easier to maintain and extend. |
| **Advantage** | Multiple views can be created for the same model data. For example, the same `Appointment` model is displayed differently on the dashboard (summary view) versus the search result page (detailed view). |
| **Advantage** | The REST controller (`AppointmentController`) enables the system to serve both the web UI and external API clients simultaneously, demonstrating the distributed application requirement. |
| **Limitation** | As the application grows, controllers can become "fat" with too many responsibilities. The use of a dedicated service layer (Facade pattern) mitigates this in our system. |

---

### 1.4 Facade Pattern — Service Layer Classes

**Category:** Structural Pattern

**Description:** The Facade pattern provides a unified, simplified interface to a complex subsystem. In this system, the service classes act as facades, hiding the complexity of validation, DAO interactions, and business rule enforcement behind simple method signatures.

**Implementation in the System:**

```java
// File: com.sunrisedental.service.AppointmentService (Facade)
@Service
public class AppointmentService {

    private final AppointmentDAO appointmentDAO;
    private final AppointmentValidator validator;

    public AppointmentService(AppointmentDAO appointmentDAO,
                              AppointmentValidator validator) {
        this.appointmentDAO = appointmentDAO;
        this.validator = validator;
    }

    /**
     * Facade method: Hides the complexity of validation,
     * conflict checking, ID generation, and persistence.
     */
    public Appointment registerAppointment(Appointment appointment) {
        // Step 1: Validate input data
        List<String> errors = validator.validateAppointment(appointment);
        if (!errors.isEmpty()) {
            throw new InvalidAppointmentException(errors);
        }

        // Step 2: Check for double booking conflict
        if (checkForConflicts(appointment)) {
            throw new InvalidAppointmentException(
                "Time slot already booked for Dr. " + appointment.getDentistName());
        }

        // Step 3: Generate unique appointment number
        appointment.setAppointmentNumber(generateAppointmentNumber(appointment));
        appointment.setStatus("SCHEDULED");

        // Step 4: Persist via DAO
        appointmentDAO.save(appointment);
        return appointment;
    }
}
```

**Critical Evaluation:**

| Aspect | Analysis |
|--------|----------|
| **Advantage** | The `AppointmentController` (Presentation Tier) simply calls `appointmentService.registerAppointment(appointment)` — it does not need to know about validation rules, conflict checking algorithms, or database operations. |
| **Advantage** | Centralises business logic, preventing duplication. Both the web servlet and the REST API controller use the same `AppointmentService`, ensuring consistent behaviour. |
| **Advantage** | Simplifies testing — the facade can be tested independently with mock DAOs (as demonstrated in the test suite). |
| **Justification** | The Facade pattern is essential for maintaining the integrity of the 3-tier architecture. Without it, controllers would directly access DAOs, violating the separation of concerns. |

---

### 1.5 Strategy Pattern — Treatment Cost Calculation

**Category:** Behavioural Pattern

**Description:** The Strategy pattern defines a family of algorithms and makes them interchangeable. In this system, the treatment cost varies based on the treatment type selected, and the billing calculation strategy adapts accordingly.

**Implementation in the System:**

The `Treatment` model encapsulates the cost strategy for each treatment type. The `BillingService` retrieves the appropriate cost from the `Treatment` object associated with an appointment, allowing different treatments to have different pricing without modifying the billing logic.

```java
// File: com.sunrisedental.service.BillingService (Strategy Consumer)
public class BillingService {

    private static final double CONSULTATION_FEE = 500.00;

    public Bill calculateBill(Appointment appointment) {
        // Strategy: Treatment cost varies by treatment type
        double treatmentCost = appointment.getTreatment().getTreatmentCost();

        // Total = fixed consultation fee + variable treatment cost
        Bill bill = new Bill(appointment, CONSULTATION_FEE, treatmentCost);
        return bill;
    }
}
```

The treatment types and costs are stored in the database, allowing new treatments to be added without code changes:

```sql
-- File: schema.sql (Treatment data)
INSERT INTO treatments (treatment_type, treatment_cost, duration_minutes) VALUES
    ('Consultation Only',  0.00,     15),
    ('Teeth Cleaning',     3000.00,  30),
    ('Root Canal',         15000.00, 60),
    ('Dental Implant',     75000.00, 120);
```

**Critical Evaluation:**

| Aspect | Analysis |
|--------|----------|
| **Advantage** | New treatment types can be added to the database without any code modifications, adhering to the **Open/Closed Principle**. |
| **Advantage** | The `BillingService` remains stable regardless of how many treatment types exist or how their costs change over time. |
| **Justification** | This approach is more maintainable than using conditional statements (if/else or switch) to determine costs for each treatment type. |

---

## 2. Three-Tier Architecture Implementation

The Sunrise Dental Clinic system strictly implements a **3-tier distributed architecture**, separating the application into three logically independent layers. Each tier has a well-defined responsibility and communicates only with its adjacent tier, ensuring unidirectional dependency flow.

### 2.1 Architecture Overview

```
┌──────────────────────────────────────────────────────────────┐
│                     PRESENTATION TIER                        │
│              (User Interface / Web Layer)                     │
│                                                              │
│   JSP Pages:  login.jsp, dashboard.jsp, billing.jsp,         │
│               register-appointment.jsp, search-result.jsp,   │
│               help.jsp                                       │
│   Servlets:   LoginServlet, DashboardServlet, BillingServlet,│
│               AppointmentServlet, SearchServlet              │
│   REST API:   AppointmentController (/api/appointments)      │
│   Swing GUI:  LoginScreen, DashboardScreen,                  │
│               RegisterAppointmentScreen                      │
├──────────────────────────────────────────────────────────────┤
│                   BUSINESS LOGIC TIER                        │
│             (Services + Domain Model)                         │
│                                                              │
│   Services:   AppointmentService, BillingService,            │
│               AppointmentValidator                           │
│   Models:     Patient, Appointment, Treatment, Bill, User    │
│   Exceptions: InvalidAppointmentException,                   │
│               AppointmentNotFoundException                   │
├──────────────────────────────────────────────────────────────┤
│                    DATA ACCESS TIER                           │
│               (Persistence / Database)                        │
│                                                              │
│   DAOs:       AppointmentDAO (interface),                    │
│               AppointmentDAOImpl (JDBC implementation)        │
│   Config:     DatabaseConnection (Singleton)                 │
│   Database:   MySQL 8.0 (schema.sql)                         │
│               H2 (embedded, for testing)                     │
└──────────────────────────────────────────────────────────────┘
```

### 2.2 Tier Communication Flow

The following table demonstrates how a typical user action flows through all three tiers:

| Step | Tier | Component | Action |
|------|------|-----------|--------|
| 1 | Presentation | `register-appointment.jsp` | User fills in appointment form and clicks "Register" |
| 2 | Presentation | `AppointmentServlet` | Servlet receives HTTP POST, extracts form parameters |
| 3 | Business Logic | `AppointmentService` | Service validates data, checks conflicts, generates appointment number |
| 4 | Business Logic | `AppointmentValidator` | Validates patient name, contact, date, time format and ranges |
| 5 | Data Access | `AppointmentDAOImpl` | Executes `INSERT INTO appointments` via JDBC PreparedStatement |
| 6 | Data Access | `DatabaseConnection` | Provides the database connection (Singleton) |
| 7 | Presentation | `dashboard.jsp` | Redirects to dashboard with success confirmation |

### 2.3 Critical Evaluation of the 3-Tier Architecture

**Strengths:**

- **Separation of Concerns:** Each tier can be modified independently. For example, migrating from JSP to a React frontend would require changes only in the Presentation Tier — the Business Logic and Data Access Tiers remain untouched.
- **Testability:** The Business Logic Tier is tested independently using JUnit 5, with no dependency on the web server or database. This is demonstrated by the 37 passing unit tests.
- **Scalability:** The REST API (`AppointmentController`) allows the business logic to be consumed by mobile apps, third-party integrations, or microservices in the future.
- **Security:** Session management and authentication checks are enforced at the Presentation Tier (servlets), preventing unauthorized access to business logic operations.

**Limitations:**

- The current system uses synchronous request-response communication. For a larger clinic network, asynchronous messaging (e.g., RabbitMQ) would improve resilience.
- The Data Access Tier uses raw JDBC rather than an ORM. While this demonstrates the DAO pattern clearly, an ORM like Hibernate would reduce boilerplate for a production system.

---

## 3. Distributed Application with Web Services

The system satisfies the distributed application requirement through a RESTful API layer built with Spring Boot's `@RestController`.

### 3.1 REST API Endpoints

| HTTP Method | Endpoint | Description | Request Body | Response |
|-------------|----------|-------------|--------------|----------|
| `POST` | `/api/appointments` | Register a new appointment | JSON `Appointment` object | `201 Created` with saved appointment |
| `GET` | `/api/appointments/{number}` | Retrieve appointment by number | — | `200 OK` with appointment details |

### 3.2 REST Controller Implementation

```java
// File: com.sunrisedental.controller.AppointmentController
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Constructor injection (Dependency Inversion)
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<?> registerAppointment(@RequestBody Appointment appointment) {
        try {
            Appointment saved = appointmentService.registerAppointment(appointment);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (InvalidAppointmentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Validation Failed");
            error.put("messages", e.getValidationErrors());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{appointmentNumber}")
    public ResponseEntity<?> getAppointmentDetails(
            @PathVariable String appointmentNumber) {
        Optional<Appointment> appointment =
            appointmentService.searchByAppointmentNumber(appointmentNumber);

        if (appointment.isPresent()) {
            return new ResponseEntity<>(appointment.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                Map.of("message", "Appointment not found"),
                HttpStatus.NOT_FOUND);
        }
    }
}
```

**How This Satisfies the Distributed Requirement:**

The REST API enables the system to be consumed by multiple clients simultaneously — the web browser (via JSP forms), the Swing desktop GUI (via `RestApiClient`), and any external system that can make HTTP requests. This is the fundamental characteristic of a distributed application.

---

## 4. Database Design

### 4.1 Entity-Relationship Summary

The database schema (`schema.sql`) consists of 5 normalised tables with referential integrity enforced through foreign keys:

| Table | Purpose | Key Relationships |
|-------|---------|-------------------|
| `users` | Authentication & role-based access (ADMIN, RECEPTIONIST, DENTIST) | — |
| `patients` | Patient demographic records | Referenced by `appointments` |
| `treatments` | Predefined treatment types with fixed costs | Referenced by `appointments` |
| `appointments` | Core appointment records linking patients, dentists, and treatments | FK → `patients`, FK → `treatments` |
| `bills` | Generated invoices for completed appointments | FK → `appointments` (1:1) |

### 4.2 Advanced Database Features

**Stored Procedure — Appointment Number Generation:**

```sql
CREATE PROCEDURE GenerateAppointmentNumber(
    IN p_date DATE,
    OUT p_appointment_number VARCHAR(20)
)
BEGIN
    DECLARE v_count INT;
    SELECT COUNT(*) + 1 INTO v_count
    FROM appointments WHERE appointment_date = p_date;

    SET p_appointment_number = CONCAT(
        'APT-', DATE_FORMAT(p_date, '%Y%m%d'), '-',
        LPAD(v_count, 3, '0')
    );
END
```

This stored procedure encapsulates the business rule for generating unique, date-based appointment numbers directly in the database, ensuring consistency even when multiple clients access the system simultaneously.

**Trigger — Auto-Complete Appointment on Payment:**

```sql
CREATE TRIGGER after_bill_paid
AFTER UPDATE ON bills
FOR EACH ROW
BEGIN
    IF NEW.payment_status = 'PAID' AND OLD.payment_status != 'PAID' THEN
        UPDATE appointments
        SET status = 'COMPLETED', updated_at = CURRENT_TIMESTAMP
        WHERE appointment_id = NEW.appointment_id;
    END IF;
END
```

This trigger automatically updates the appointment status to `COMPLETED` when a bill is marked as `PAID`, enforcing the business rule that payment completion signifies appointment completion without requiring application-level code.

### 4.3 Indexes for Performance

```sql
INDEX idx_appointment_number (appointment_number)   -- Fast lookup by appointment number
INDEX idx_appointment_date (appointment_date)        -- Daily schedule queries
INDEX idx_appointment_dentist_date (dentist_name, appointment_date)  -- Conflict detection
INDEX idx_patients_contact (contact_number)          -- Patient search by phone
```

---

## 5. Session Management & Security

### 5.1 HTTP Session Management

The system uses Jakarta Servlet HTTP sessions to maintain user state across requests. This is critical for a multi-page web application where the user must remain authenticated.

```java
// File: com.sunrisedental.servlet.LoginServlet
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        boolean isAuthenticated = "admin".equals(username) && "admin123".equals(password);

        if (isAuthenticated) {
            // Create session and store user attributes
            HttpSession session = request.getSession(true);
            session.setAttribute("username", username);
            session.setAttribute("role", "ADMIN");
            session.setMaxInactiveInterval(30 * 60);  // 30-minute timeout

            response.sendRedirect("dashboard");
        } else {
            request.setAttribute("error", "Invalid username or password.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
```

### 5.2 Session Validation on Every Page

Every protected servlet checks for a valid session before allowing access:

```java
// Session check pattern used across all servlets
HttpSession session = request.getSession(false);  // Do NOT create new session
if (session == null || session.getAttribute("username") == null) {
    response.sendRedirect("login.jsp");  // Redirect to login
    return;
}
```

### 5.3 Secure Logout

```java
// Logout: Invalidate session and redirect to login
if ("logout".equals(action)) {
    HttpSession session = request.getSession(false);
    if (session != null) {
        session.invalidate();  // Destroy all session data
    }
    response.sendRedirect("login.jsp");
}
```

---

## 6. User Interface Design

The web interface is built using JSP pages with embedded CSS styling, providing a professional and user-friendly experience. Each page serves a specific functional requirement from the case study:

| Page | Functionality | Key Features |
|------|--------------|--------------|
| `login.jsp` | User authentication | Username/password form, error messages, clean design |
| `dashboard.jsp` | Main navigation hub | Quick-action cards, welcome message, session-aware |
| `register-appointment.jsp` | Appointment registration | Form validation, dropdown menus, date/time pickers |
| `search-result.jsp` | Display appointment details | Search by appointment number, full record display |
| `billing.jsp` | Calculate and display bill | Cost breakdown, receipt generation, formatted output |
| `help.jsp` | Step-by-step instructions | Numbered guide for new staff |

### Validation Mechanisms

Client-side validation is implemented in the registration form to prevent invalid submissions:

- Patient name must not be empty
- Contact number must be exactly 10 digits starting with 0 (Sri Lankan format)
- Appointment date cannot be in the past
- Appointment time must be within clinic hours (08:00 - 20:00)
- Dentist and treatment type must be selected from dropdown lists

Server-side validation in `AppointmentValidator.java` provides a second layer of protection, ensuring data integrity even if client-side validation is bypassed.

---

## 7. Summary of Design Patterns Applied

| # | Pattern | Type | Where Applied | Impact |
|---|---------|------|---------------|--------|
| 1 | **Singleton** | Creational | `DatabaseConnection` | Prevents resource exhaustion by reusing a single DB connection |
| 2 | **DAO** | Structural | `AppointmentDAO` / `AppointmentDAOImpl` | Decouples business logic from database, enables testing |
| 3 | **MVC** | Architectural | Servlets (Controller) + JSPs (View) + Models | Separates UI from logic, supports multiple interfaces |
| 4 | **Facade** | Structural | `AppointmentService`, `BillingService` | Simplifies complex operations behind clean API |
| 5 | **Strategy** | Behavioural | `Treatment` cost via `BillingService` | Treatment costs are data-driven, not hard-coded |

---

*End of Phase 2: Design Patterns & Architecture Development*
