package com.sunrisedental.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * HTTP Client Utility for the GUI Tier.
 * 
 * Handles all communication between the Desktop Client (Swing GUI) and
 * the Backend Web Services (Spring Boot REST APIs). Uses standard Java 11+
 * HttpClient and Jackson ObjectMapper for JSON serialization/deserialization.
 */
public class RestApiClient {

    private static final String BASE_URL = "http://localhost:8080/api";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public RestApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
                
        this.objectMapper = new ObjectMapper();
        // Register module to handle Java 8 Date/Time API (LocalDate, LocalTime)
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Sends a POST request to the specified endpoint with the given payload.
     * 
     * @param endpoint the API endpoint (e.g., "/appointments")
     * @param requestBody the object to serialize as JSON and send
     * @param responseType the class of the expected response object
     * @return the deserialized response object
     * @throws Exception if network or parsing error occurs
     */
    public <T> T post(String endpoint, Object requestBody, Class<T> responseType) throws Exception {
        String jsonPayload = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return objectMapper.readValue(response.body(), responseType);
        } else {
            throw new RuntimeException("API Error (HTTP " + response.statusCode() + "): " + response.body());
        }
    }

    /**
     * Sends a GET request to the specified endpoint.
     * 
     * @param endpoint the API endpoint (e.g., "/appointments/APT-123")
     * @param responseType the class of the expected response object
     * @return the deserialized response object
     * @throws Exception if network or parsing error occurs
     */
    public <T> T get(String endpoint, Class<T> responseType) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), responseType);
        } else if (response.statusCode() == 404) {
            throw new RuntimeException("Resource not found (HTTP 404)");
        } else {
            throw new RuntimeException("API Error (HTTP " + response.statusCode() + "): " + response.body());
        }
    }
}
