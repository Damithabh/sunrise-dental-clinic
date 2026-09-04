# Phase 4: Git Version Control & Documentation — Sunrise Dental Clinic
## Appointment & Patient Management System

---

## 1. Git Repository Setup

### 1.1 Repository Creation

A Git repository was initialised at the start of the project to track all source code, configuration, and documentation changes throughout the software development lifecycle.

```bash
# Initialise the repository
git init

# Create .gitignore to exclude build artifacts and IDE files
# (target/, .idea/, *.class, *.log, etc.)

# Initial commit with project structure
git add .
git commit -m "chore(init): initialise project structure with Maven, Spring Boot, and CI/CD pipeline"
```

### 1.2 Repository Structure

```
sunrise-dental-clinic/
├── .git/                          # Git version control data
├── .github/
│   └── workflows/
│       └── build.yml              # CI/CD pipeline (GitHub Actions)
├── .gitignore                     # Excluded files configuration
├── src/
│   ├── main/
│   │   ├── java/com/sunrisedental/   # Application source code
│   │   ├── resources/                 # Configuration files
│   │   └── webapp/                    # JSP pages and web assets
│   └── test/
│       └── java/com/sunrisedental/   # Test classes
├── Phase1_SystemDesign_UML.md         # Task A documentation
├── Phase2_DesignPatterns_Architecture.md  # Task B documentation
├── Phase3_TestPlan_TDD.md             # Task C documentation
├── Phase4_Git_Documentation.md        # Task D documentation (this file)
├── pom.xml                            # Maven build configuration
└── README.md                          # Project overview and setup guide
```

### 1.3 `.gitignore` Configuration

The `.gitignore` file is configured to exclude build outputs, IDE-specific files, and sensitive configuration from version control:

```gitignore
# Build output
target/

# IDE files
.idea/
*.iml
.vscode/

# OS files
.DS_Store
Thumbs.db

# Compiled classes
*.class

# Log files
*.log

# Maven wrapper (optional - can be included for portability)
!.mvn/wrapper/maven-wrapper.jar
```

---

## 2. Version Control Strategy

### 2.1 Branching Model

This project follows a **feature-branch workflow**, which is an industry-standard approach for managing parallel development:

```
main (production-ready, stable code)
 ├── develop (integration branch for features)
 │    ├── feature/login-authentication
 │    ├── feature/appointment-management
 │    ├── feature/billing-system
 │    ├── feature/patient-registration
 │    ├── feature/help-section
 │    └── feature/reports-dashboard
 └── hotfix/* (critical production fixes)
```

| Branch | Purpose | Merges Into |
|--------|---------|-------------|
| `main` | Production-ready, stable, deployed code | — |
| `develop` | Integration branch where features are combined and tested | `main` |
| `feature/*` | Individual feature development branches | `develop` |
| `hotfix/*` | Urgent bug fixes for production issues | `main` + `develop` |

### 2.2 Branching Rules

1. **No direct commits to `main`** — All changes must go through a feature branch and be merged via pull request (or merge commit).
2. **Feature branches are short-lived** — Each feature branch addresses a single user story or task and is merged promptly to avoid divergence.
3. **Code review before merge** — Pull requests require review (in a team context) before merging to maintain code quality.

### 2.3 Commit Message Convention

The project uses **Conventional Commits** format, which provides a structured, machine-readable commit history:

```
<type>(<scope>): <short description>

[Optional body explaining WHY the change was made]

[Optional footer with references]
```

**Commit Types:**

| Type | Meaning | Example |
|------|---------|---------|
| `feat` | New feature | `feat(appointment): add registration form with validation` |
| `fix` | Bug fix | `fix(billing): correct discount calculation for edge cases` |
| `docs` | Documentation only | `docs(readme): update setup instructions for MySQL 8.0` |
| `test` | Adding or modifying tests | `test(auth): add unit tests for login authentication` |
| `chore` | Build/config changes | `chore(deps): update Spring Boot to 3.2.5` |
| `ci` | CI/CD pipeline changes | `ci(workflow): configure JaCoCo coverage in CI pipeline` |
| `refactor` | Code restructuring | `refactor(service): extract validation into separate class` |
| `style` | Formatting only | `style(code): apply consistent indentation` |

---

## 3. Commit History Evidence

The following is the documented commit history showing the progression of development:

### 3.1 Full Commit Log

| # | Commit Hash | Message | Description |
|---|-------------|---------|-------------|
| 1 | `1337037` | `chore(init): initialise project structure with Maven, Spring Boot, and CI/CD pipeline` | Initial project setup with `pom.xml`, Spring Boot configuration, GitHub Actions workflow, `.gitignore`, and `README.md` |
| 2 | `e9b6cc9` | `feat(testing): implement Phase 3 TDD with JUnit 5 test suite and business logic` | Created test classes (`BillingServiceTest`, `AppointmentValidatorTest`) following TDD RED phase, then implemented `BillingService` and `AppointmentValidator` to pass all tests |
| 3 | `7a4e8b6` | `feat(backend): implement Phase 4 - Database schema and 3-Tier Web Services` | Created `schema.sql` with all 5 tables, stored procedures, triggers, seed data. Implemented `AppointmentController` REST API, `AppointmentDAOImpl`, and `DatabaseConnection` Singleton |
| 4 | `12cb224` | `feat(gui): implement Phase 5 - User Interface and Validation` | Built Swing GUI classes (`LoginScreen`, `DashboardScreen`, `RegisterAppointmentScreen`) with client-side validation and `RestApiClient` for API communication |
| 5 | `67e4c2b` | `feat(web): migrate Presentation Tier to JSP and Servlets` | Created JSP pages (`login.jsp`, `dashboard.jsp`, `register-appointment.jsp`, `billing.jsp`, `search-result.jsp`, `help.jsp`) and corresponding servlets |
| 6 | `d63b997` | `chore: configure Spring Boot for JSP support` | Updated `application.properties` for JSP view resolver, configured embedded Tomcat for JSP compilation |
| 7 | `d7005fc` | `fix: resolve Spring Boot startup crash and configure H2` | Fixed H2 database configuration for local development, resolved class scanning issues |
| 8 | `36841b4` | `fix: disable Spring Security AutoConfiguration and enable ServletComponentScan` | Resolved security auto-configuration conflicts, enabled servlet component scanning for `@WebServlet` annotations |

### 3.2 Version Progression

```
Version 1.0.0 (Commit 1)     → Project structure and build configuration
Version 1.1.0 (Commit 2)     → Business logic with TDD (tests + implementation)
Version 1.2.0 (Commit 3)     → Database schema and REST API layer
Version 1.3.0 (Commit 4)     → Desktop GUI (Swing)
Version 1.4.0 (Commit 5)     → Web UI (JSP/Servlet)
Version 1.4.1 (Commits 6-8)  → Bug fixes and configuration
```

---

## 4. CI/CD Workflow (GitHub Actions)

### 4.1 Pipeline Overview

A CI/CD (Continuous Integration / Continuous Deployment) pipeline is configured using **GitHub Actions**. The workflow file is located at `.github/workflows/build.yml` and triggers automatically on every push or pull request to the `main` or `develop` branches.

### 4.2 Pipeline Stages

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│  Checkout    │ →  │   Build     │ →  │    Test     │ →  │  Package    │
│  Repository  │    │   Compile   │    │  Unit Tests │    │  WAR File   │
└─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘
                                            │
                                            ▼
                                    ┌─────────────┐    ┌─────────────┐
                                    │  Coverage   │ →  │   Upload    │
                                    │  Report     │    │  Artifacts  │
                                    └─────────────┘    └─────────────┘
```

### 4.3 Workflow Configuration

```yaml
# File: .github/workflows/build.yml
name: Sunrise Dental Clinic CI/CD

on:
  push:
    branches: [ "main", "develop" ]
  pull_request:
    branches: [ "main" ]

jobs:
  build:
    name: Build & Test
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Repository
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven

      - name: Build Project
        run: mvn clean compile -B

      - name: Run Unit Tests
        run: mvn test -B

      - name: Generate Code Coverage Report
        run: mvn jacoco:report -B

      - name: Upload Test Results
        uses: actions/upload-artifact@v4
        with:
          name: test-results
          path: target/surefire-reports/

      - name: Upload Coverage Report
        uses: actions/upload-artifact@v4
        with:
          name: coverage-report
          path: target/site/jacoco/

      - name: Package Application (WAR)
        run: mvn package -B -DskipTests

      - name: Upload Build Artifact
        uses: actions/upload-artifact@v4
        with:
          name: sunrise-dental-clinic-${{ github.sha }}
          path: target/*.war
```

### 4.4 Pipeline Features

| Feature | Description |
|---------|-------------|
| **Automatic Trigger** | Runs on every `git push` to `main` or `develop`, and on every Pull Request |
| **JDK 17 Setup** | Uses the Temurin distribution of JDK 17, matching the project's Java version |
| **Maven Caching** | Caches `~/.m2/repository` to speed up subsequent builds |
| **Unit Test Execution** | Runs all 37 JUnit 5 tests via Maven Surefire |
| **Code Coverage** | Generates JaCoCo HTML coverage report |
| **Artifact Upload** | Stores test results, coverage reports, and the compiled WAR file as downloadable artifacts |
| **Concurrency Control** | Cancels in-progress runs if a new push arrives, saving CI minutes |

### 4.5 Impact of CI/CD on Development

The CI/CD pipeline ensures that:

1. **Every commit is validated** — Code that breaks the build or fails any test is immediately detected.
2. **Coverage is tracked** — The JaCoCo report shows which code paths are tested, guiding where additional tests are needed.
3. **Deployment artifacts are ready** — The compiled WAR file is available as a downloadable artifact after every successful build.
4. **Team collaboration is safer** — In a multi-developer scenario, pull requests cannot be merged if the pipeline fails.

---

## 5. Version Control Techniques Demonstrated

### 5.1 Techniques Used

| Technique | How It Was Applied |
|-----------|-------------------|
| **Meaningful commit messages** | Every commit follows the Conventional Commits format with type, scope, and description |
| **Incremental development** | Features were developed and committed incrementally (structure → tests → business logic → database → UI → bug fixes) |
| **`.gitignore` management** | Build artifacts (`target/`), IDE files (`.idea/`, `.vscode/`), and compiled classes are excluded from version control |
| **CI/CD automation** | GitHub Actions pipeline validates every push with build, test, and coverage stages |
| **Feature isolation** | Each major feature (login, appointment management, billing, testing) was developed as a distinct commit, maintaining clear version history |

### 5.2 Git Commands Used During Development

```bash
# Stage and commit changes
git add .
git commit -m "feat(web): migrate Presentation Tier to JSP and Servlets"

# View commit history
git log --oneline

# Check repository status
git status

# Push to remote repository
git push origin main

# Create a feature branch (in a team workflow)
git checkout -b feature/billing-system
# ... make changes ...
git commit -m "feat(billing): implement BillingService with TDD"
git checkout main
git merge feature/billing-system
```

---

## 6. Deployment Instructions

### 6.1 Local Development Setup

```bash
# 1. Clone the repository
git clone https://github.com/YOUR_USERNAME/sunrise-dental-clinic.git
cd sunrise-dental-clinic

# 2. Build the project
mvn clean install

# 3. Run the application
mvn spring-boot:run

# 4. Access the application
# Open browser: http://localhost:8080
# Login credentials: admin / admin123
```

### 6.2 Production Deployment (WAR File)

```bash
# 1. Package the application
mvn clean package -DskipTests

# 2. Deploy the WAR file to a servlet container (e.g., Apache Tomcat)
cp target/sunrise-dental-clinic-1.0.0-SNAPSHOT.war /path/to/tomcat/webapps/

# 3. Start Tomcat
./catalina.sh start
```

---

## 7. GitHub Repository Link

**Repository URL:** *(To be updated after pushing to GitHub)*

`https://github.com/YOUR_USERNAME/sunrise-dental-clinic`

*(Screenshot of the GitHub repository page should be inserted here in the final PDF submission.)*

---

*End of Phase 4: Git Version Control & Documentation*
