package com.sunrisedental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Sunrise Dental Clinic — Main Application Entry Point.
 * 
 * This is the bootstrap class for the Spring Boot distributed application.
 * It initialises the Spring context, auto-configures components, and starts
 * the embedded web server.
 * 
 * Architecture: 3-Tier Distributed
 *   - Presentation Tier:   Spring MVC Controllers + Thymeleaf Templates
 *   - Business Logic Tier: Spring Service Layer + Domain Entities
 *   - Data Access Tier:    Spring Data JPA Repositories + MySQL
 * 
 * @author Sunrise Dental Clinic Development Team
 * @version 1.0.0-SNAPSHOT
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ServletComponentScan
public class SunriseDentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SunriseDentalApplication.class, args);
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("  Sunrise Dental Clinic System Started");
        System.out.println("  Access: http://localhost:8080");
        System.out.println("═══════════════════════════════════════════════");
    }
}
