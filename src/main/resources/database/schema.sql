-- ═══════════════════════════════════════════════════════════════
--  SUNRISE DENTAL CLINIC — Database Schema
--  Database: MySQL 8.0+
--  Version:  1.0.0
--
--  Tables:  users, patients, treatments, appointments, bills
--  Includes: Primary keys, foreign keys, indexes, seed data
-- ═══════════════════════════════════════════════════════════════

-- ── Create Database ──
CREATE DATABASE IF NOT EXISTS sunrise_dental_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE sunrise_dental_db;

-- ═══════════════════════════════════════════════════════════════
--  TABLE 1: users (Authentication & Role-Based Access)
-- ═══════════════════════════════════════════════════════════════
CREATE TABLE IF NOT EXISTS users (
    user_id         INT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(50)  NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    full_name       VARCHAR(100) NOT NULL,
    email           VARCHAR(100),
    role            ENUM('ADMIN', 'RECEPTIONIST', 'DENTIST') NOT NULL DEFAULT 'RECEPTIONIST',
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_users_username (username),
    INDEX idx_users_role (role)
) ENGINE=InnoDB;

-- ═══════════════════════════════════════════════════════════════
--  TABLE 2: patients
-- ═══════════════════════════════════════════════════════════════
CREATE TABLE IF NOT EXISTS patients (
    patient_id      INT AUTO_INCREMENT PRIMARY KEY,
    patient_name    VARCHAR(100) NOT NULL,
    address         VARCHAR(255),
    contact_number  VARCHAR(15)  NOT NULL,
    email           VARCHAR(100),
    date_of_birth   DATE,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE INDEX idx_patients_contact (contact_number),
    INDEX idx_patients_name (patient_name)
) ENGINE=InnoDB;

-- ═══════════════════════════════════════════════════════════════
--  TABLE 3: treatments (Predefined treatment types with costs)
-- ═══════════════════════════════════════════════════════════════
CREATE TABLE IF NOT EXISTS treatments (
    treatment_id        INT AUTO_INCREMENT PRIMARY KEY,
    treatment_type      VARCHAR(100) NOT NULL UNIQUE,
    description         VARCHAR(255),
    treatment_cost      DECIMAL(10, 2) NOT NULL,
    duration_minutes    INT DEFAULT 30,

    INDEX idx_treatments_type (treatment_type)
) ENGINE=InnoDB;

-- ═══════════════════════════════════════════════════════════════
--  TABLE 4: appointments
--  Foreign Keys: patient_id -> patients, treatment_id -> treatments
-- ═══════════════════════════════════════════════════════════════
CREATE TABLE IF NOT EXISTS appointments (
    appointment_id      INT AUTO_INCREMENT PRIMARY KEY,
    appointment_number  VARCHAR(20) NOT NULL UNIQUE,
    patient_id          INT NOT NULL,
    dentist_name        VARCHAR(100) NOT NULL,
    treatment_id        INT NOT NULL,
    appointment_date    DATE NOT NULL,
    appointment_time    TIME NOT NULL,
    status              ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'SCHEDULED',
    notes               TEXT,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_appointment_patient
        FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,

    CONSTRAINT fk_appointment_treatment
        FOREIGN KEY (treatment_id) REFERENCES treatments(treatment_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,

    UNIQUE INDEX idx_appointment_number (appointment_number),
    INDEX idx_appointment_date (appointment_date),
    INDEX idx_appointment_dentist_date (dentist_name, appointment_date)
) ENGINE=InnoDB;

-- ═══════════════════════════════════════════════════════════════
--  TABLE 5: bills
--  Foreign Key: appointment_id -> appointments
-- ═══════════════════════════════════════════════════════════════
CREATE TABLE IF NOT EXISTS bills (
    bill_id             INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id      INT NOT NULL,
    consultation_fee    DECIMAL(10, 2) NOT NULL DEFAULT 500.00,
    treatment_cost      DECIMAL(10, 2) NOT NULL,
    discount_percentage DECIMAL(5, 2)  NOT NULL DEFAULT 0.00,
    total_amount        DECIMAL(10, 2) NOT NULL,
    bill_date           DATE NOT NULL,
    payment_status      ENUM('UNPAID', 'PAID', 'PARTIAL') NOT NULL DEFAULT 'UNPAID',
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_bill_appointment
        FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,

    UNIQUE INDEX idx_bill_appointment (appointment_id),
    INDEX idx_bill_date (bill_date)
) ENGINE=InnoDB;


-- ═══════════════════════════════════════════════════════════════
--  SEED DATA
-- ═══════════════════════════════════════════════════════════════

-- ── Default Users (password: 'admin123' hashed with BCrypt) ──
INSERT INTO users (username, password_hash, full_name, email, role) VALUES
    ('admin',       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System Administrator',  'admin@sunrisedental.lk',     'ADMIN'),
    ('receptionist', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Kumari Fernando',       'kumari@sunrisedental.lk',    'RECEPTIONIST'),
    ('dr.silva',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dr. Anura Silva',       'dr.silva@sunrisedental.lk',  'DENTIST'),
    ('dr.fernando', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dr. Nimal Fernando',    'dr.fernando@sunrisedental.lk','DENTIST');

-- ── Predefined Treatment Types with Costs (LKR) ──
INSERT INTO treatments (treatment_type, description, treatment_cost, duration_minutes) VALUES
    ('Consultation Only',   'General dental consultation and examination',           0.00,    15),
    ('Teeth Cleaning',      'Professional dental cleaning and polishing',            3000.00, 30),
    ('Teeth Whitening',     'Professional teeth whitening procedure',                5000.00, 45),
    ('Dental Filling',      'Composite resin filling for cavities',                  4000.00, 30),
    ('Root Canal',          'Root canal treatment for infected teeth',               15000.00, 60),
    ('Tooth Extraction',    'Simple tooth extraction under local anaesthesia',       2500.00, 20),
    ('Dental Crown',        'Porcelain or ceramic crown placement',                  12000.00, 45),
    ('Dental Bridge',       'Fixed dental bridge for missing teeth',                 20000.00, 60),
    ('Orthodontic Braces',  'Metal or ceramic braces installation',                  50000.00, 90),
    ('Dental Implant',      'Titanium dental implant with crown',                    75000.00, 120),
    ('Gum Treatment',       'Periodontal scaling and root planing',                  6000.00, 45),
    ('Wisdom Tooth Removal','Surgical extraction of impacted wisdom teeth',          8000.00, 60);

-- ── Sample Patients ──
INSERT INTO patients (patient_name, address, contact_number, email) VALUES
    ('Kamal Perera',        '123 Galle Road, Colombo 03',       '0771234567', 'kamal@email.com'),
    ('Nimal Jayawardena',   '45 Temple Road, Kandy',            '0712345678', 'nimal@email.com'),
    ('Saman De Silva',      '78 Main Street, Gampaha',          '0761112233', 'saman@email.com');

-- ── Sample Appointments ──
INSERT INTO appointments (appointment_number, patient_id, dentist_name, treatment_id, appointment_date, appointment_time, status) VALUES
    ('APT-20240715-001', 1, 'Dr. Anura Silva',   2, '2024-07-15', '10:00:00', 'COMPLETED'),
    ('APT-20240715-002', 2, 'Dr. Nimal Fernando', 5, '2024-07-15', '14:30:00', 'COMPLETED'),
    ('APT-20240716-001', 3, 'Dr. Anura Silva',   3, '2024-07-16', '09:00:00', 'SCHEDULED');

-- ── Sample Bills ──
INSERT INTO bills (appointment_id, consultation_fee, treatment_cost, discount_percentage, total_amount, bill_date, payment_status) VALUES
    (1, 500.00, 3000.00, 0.00,  3500.00,  '2024-07-15', 'PAID'),
    (2, 500.00, 15000.00, 10.00, 13950.00, '2024-07-15', 'PAID');


-- ═══════════════════════════════════════════════════════════════
--  STORED PROCEDURE: Generate unique appointment number
--  Demonstrates advanced database features
-- ═══════════════════════════════════════════════════════════════
DELIMITER //

CREATE PROCEDURE IF NOT EXISTS GenerateAppointmentNumber(
    IN p_date DATE,
    OUT p_appointment_number VARCHAR(20)
)
BEGIN
    DECLARE v_count INT;

    -- Count existing appointments for the given date
    SELECT COUNT(*) + 1 INTO v_count
    FROM appointments
    WHERE appointment_date = p_date;

    -- Format: APT-YYYYMMDD-NNN
    SET p_appointment_number = CONCAT(
        'APT-',
        DATE_FORMAT(p_date, '%Y%m%d'),
        '-',
        LPAD(v_count, 3, '0')
    );
END //

DELIMITER ;


-- ═══════════════════════════════════════════════════════════════
--  TRIGGER: Auto-update appointment status after bill is paid
--  Demonstrates trigger usage for business rule enforcement
-- ═══════════════════════════════════════════════════════════════
DELIMITER //

CREATE TRIGGER IF NOT EXISTS after_bill_paid
AFTER UPDATE ON bills
FOR EACH ROW
BEGIN
    IF NEW.payment_status = 'PAID' AND OLD.payment_status != 'PAID' THEN
        UPDATE appointments
        SET status = 'COMPLETED', updated_at = CURRENT_TIMESTAMP
        WHERE appointment_id = NEW.appointment_id;
    END IF;
END //

DELIMITER ;


-- ═══════════════════════════════════════════════════════════════
--  Verification: Show table structure and record counts
-- ═══════════════════════════════════════════════════════════════
SELECT 'users' AS table_name, COUNT(*) AS record_count FROM users
UNION ALL
SELECT 'patients', COUNT(*) FROM patients
UNION ALL
SELECT 'treatments', COUNT(*) FROM treatments
UNION ALL
SELECT 'appointments', COUNT(*) FROM appointments
UNION ALL
SELECT 'bills', COUNT(*) FROM bills;
