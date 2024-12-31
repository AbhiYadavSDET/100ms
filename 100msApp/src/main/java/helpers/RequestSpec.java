package helpers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import utils.ConfigManager;

public class RequestSpec {

    // Fetch values from ConfigManager
    private static String baseUrl = ConfigManager.get("base.url");
    private static String managementToken = ConfigManager.get("management.token");

    // Static method to return a RequestSpecification instance
    public static RequestSpecification getRequestSpec() {
        // Check if baseUrl or managementToken is missing
        if (baseUrl == null || managementToken == null) {
            throw new IllegalArgumentException("Configuration values for base URL or management token are missing.");
        }

        // Return the RequestSpecification with necessary configurations
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl) // Set the base URI
                .addHeader("Authorization", "Bearer " + managementToken) // Add Authorization header
                .setContentType("application/json") // Set content type to JSON
                .build(); // Return the RequestSpecification object
    }
}
