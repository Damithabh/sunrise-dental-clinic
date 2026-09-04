# 🦷 Sunrise Dental Clinic — Appointment & Patient Management System

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL"/>
  <img src="https://img.shields.io/badge/Maven-3.9+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven"/>
  <img src="https://img.shields.io/badge/License-Academic-blue?style=for-the-badge" alt="License"/>
</p>

<p align="center">
  <img src="https://img.shields.io/github/actions/workflow/status/Damithabh/sunrise-dental-clinic/build.yml?branch=main&style=flat-square&label=CI%2FCD%20Build" alt="Build Status"/>
  <img src="https://img.shields.io/badge/Test_Coverage-JaCoCo-green?style=flat-square" alt="Coverage"/>
</p>

---

## 📋 Project Description

**Sunrise Dental Clinic** is a distributed, web-based appointment and patient management system built using Java and Spring Boot. It replaces the clinic's existing manual, paper-based workflow — which suffered from double bookings, lost patient records, long waiting times, and billing errors — with a modern, computerized solution.

### Key Features

| Feature | Description |
|---------|-------------|
| 🔐 **User Authentication** | Secure login system with role-based access control (Receptionist, Dentist, Admin) |
| 📅 **Appointment Management** | Register, search, and manage patient appointments with conflict detection |
| 👤 **Patient Registration** | Maintain comprehensive patient records with validation |
| 💰 **Billing & Invoicing** | Automatic bill calculation based on treatment type and consultation fees |
| 📊 **Reports & Dashboards** | Daily schedules, appointment summaries, and billing reports |
| ❓ **Help System** | Built-in step-by-step user guide for staff |

---

## 🏗️ Architecture — 3-Tier Distributed Design

This system follows a **3-tier architecture** to enforce separation of concerns:

```
┌──────────────────────────────────────────────────────────┐
│                   PRESENTATION TIER                      │
│          (Spring MVC + Thymeleaf Templates)              │
│   LoginView · AppointmentView · BillView · HelpView     │
├──────────────────────────────────────────────────────────┤
│                  BUSINESS LOGIC TIER                     │
│              (Spring Services + Domain)                  │
│  AuthenticationService · AppointmentService ·            │
│  BillingService · User · Patient · Appointment ·         │
│  Treatment · Bill                                        │
├──────────────────────────────────────────────────────────┤
│                   DATA ACCESS TIER                       │
│            (Spring Data JPA + MySQL)                     │
│  UserDAO · PatientDAO · AppointmentDAO ·                 │
│  TreatmentDAO · BillDAO · DatabaseConnection(Singleton)  │
└──────────────────────────────────────────────────────────┘
```

### Design Patterns Used

| Pattern | Implementation |
|---------|---------------|
| **Singleton** | `DatabaseConnection` — single DB connection pool instance |
| **DAO (Data Access Object)** | All repository classes abstracting database operations |
| **Facade** | Service layer classes providing simplified business logic interfaces |
| **MVC (Model-View-Controller)** | Spring MVC separating presentation from business logic |
| **Strategy** | Treatment cost calculation based on treatment type |

---

## 📁 Project Structure

```
sunrise-dental-clinic/
├── .github/
│   └── workflows/
│       └── build.yml              # CI/CD pipeline (GitHub Actions)
├── src/
│   ├── main/
│   │   ├── java/com/sunrisedental/
│   │   │   ├── SunriseDentalApplication.java
│   │   │   ├── config/            # Security & app configuration
│   │   │   ├── controller/        # Presentation tier (REST/MVC controllers)
│   │   │   ├── model/             # Domain entities (Patient, Appointment, etc.)
│   │   │   ├── repository/        # Data access tier (Spring Data JPA repos)
│   │   │   ├── service/           # Business logic tier (services)
│   │   │   └── util/              # Utility classes and helpers
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── templates/         # Thymeleaf HTML templates
│   │       └── static/            # CSS, JS, images
│   └── test/
│       └── java/com/sunrisedental/
│           ├── SunriseDentalApplicationTests.java
│           ├── controller/        # Controller unit tests
│           ├── service/           # Service unit tests
│           └── repository/        # Repository integration tests
├── docs/                          # UML diagrams and design documentation
├── .gitignore
├── pom.xml                        # Maven build configuration
└── README.md
```

---

## 🚀 Setup Instructions

### Prerequisites

| Tool | Version | Download |
|------|---------|----------|
| **Java JDK** | 17+ | [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/) |
| **Apache Maven** | 3.9+ | [Maven Downloads](https://maven.apache.org/download.cgi) |
| **MySQL Server** | 8.0+ | [MySQL Downloads](https://dev.mysql.com/downloads/) |
| **Git** | 2.40+ | [Git Downloads](https://git-scm.com/downloads) |
| **IDE** (Recommended) | Latest | IntelliJ IDEA / Eclipse / VS Code |

### Step 1: Clone the Repository

```bash
git clone https://github.com/Damithabh/sunrise-dental-clinic.git
cd sunrise-dental-clinic
```

### Step 2: Configure the Database

Create a MySQL database and update the connection details:

```sql
CREATE DATABASE sunrise_dental_db;
CREATE USER 'dental_admin'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON sunrise_dental_db.* TO 'dental_admin'@'localhost';
FLUSH PRIVILEGES;
```

### Step 3: Update Application Properties

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sunrise_dental_db
spring.datasource.username=dental_admin
spring.datasource.password=your_password
```

### Step 4: Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will be available at: **http://localhost:8080**

### Step 5: Run Tests

```bash
# Run all tests
mvn test

# Run tests with coverage report
mvn test jacoco:report
```

---

## 🔄 Version Control Strategy

### Branching Model

This project follows a **feature-branch workflow**:

```
main (production-ready)
 ├── develop (integration branch)
 │    ├── feature/login-authentication
 │    ├── feature/appointment-management
 │    ├── feature/billing-system
 │    ├── feature/patient-registration
 │    ├── feature/help-section
 │    └── feature/reports-dashboard
 └── hotfix/* (critical production fixes)
```

### Branching Rules

| Branch | Purpose | Merges Into |
|--------|---------|-------------|
| `main` | Production-ready, stable code | — |
| `develop` | Integration branch for features | `main` |
| `feature/*` | Individual feature development | `develop` |
| `hotfix/*` | Critical bug fixes | `main` + `develop` |

### Commit Message Convention

```
<type>(<scope>): <short description>

[Optional body explaining WHY the change was made]

[Optional footer with references]
```

**Types:** `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`, `ci`

**Examples:**
```
feat(appointment): add appointment registration form with validation
fix(billing): correct discount calculation for senior patients
docs(readme): update setup instructions for MySQL 8.0
test(auth): add unit tests for login authentication service
ci(workflow): configure JaCoCo coverage threshold in CI pipeline
```

### CI/CD Pipeline

Every push and pull request to `main` triggers the GitHub Actions workflow which:
1. ✅ Sets up JDK 17
2. ✅ Caches Maven dependencies
3. ✅ Compiles the source code
4. ✅ Runs all unit and integration tests
5. ✅ Generates JaCoCo code coverage report
6. ✅ Uploads test results as artifacts

---

## 📝 Module Information

| Field | Details |
|-------|---------|
| **Module** | CIS 6003 — Advanced Programming |
| **Assessment** | WRIT1 (100%) |
| **Institution** | ICBT Campus / Cardiff Metropolitan University |

---

## 📄 License

This project is developed for academic purposes as part of the CIS 6003 Advanced Programming module.

---
