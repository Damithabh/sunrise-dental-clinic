/**
 * Service package — Business Logic Tier.
 * 
 * Contains Spring service classes that implement the business rules
 * and orchestrate operations between the presentation and data access tiers.
 * These classes act as a Facade, providing simplified interfaces to the
 * complex subsystem of repositories and domain objects.
 * 
 * Services in this package:
 *   - AuthenticationService: Handles user login, session, and password hashing
 *   - AppointmentService   : Manages appointment lifecycle and conflict detection
 *   - BillingService       : Calculates bills, applies discounts, generates receipts
 *   - PatientService       : Manages patient registration and lookup
 */
package com.sunrisedental.service;
