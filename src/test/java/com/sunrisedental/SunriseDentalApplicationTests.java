package com.sunrisedental;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Smoke test to verify the Spring application context loads successfully.
 * Uses the H2 in-memory database (test profile) for isolation.
 */
@SpringBootTest
@ActiveProfiles("test")
class SunriseDentalApplicationTests {

    @Test
    void contextLoads() {
        // Verifies that the Spring application context starts without errors.
        // This is a critical baseline test for any Spring Boot application.
    }
}
