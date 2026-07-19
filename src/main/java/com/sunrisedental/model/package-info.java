/**
 * Model package — Domain Entities (Business Logic Tier).
 * 
 * Contains JPA entity classes that represent the core domain model
 * of the Sunrise Dental Clinic system.
 * 
 * Entities in this package:
 *   - User        : Abstract base class for system users (inheritance)
 *   - Receptionist: Staff member who manages appointments and billing
 *   - Admin       : System administrator who manages user accounts
 *   - Dentist     : Clinical staff assigned to appointments
 *   - Patient     : Registered patient with personal details
 *   - Appointment : Scheduled appointment linking patient, dentist, and treatment
 *   - Treatment   : Available dental treatment types with costs
 *   - Bill        : Generated invoice for an appointment
 */
package com.sunrisedental.model;
