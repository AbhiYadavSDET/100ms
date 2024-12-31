package helpers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import utils.ConfigManager;

public class RequestSpec {

        private static String baseUrl = ConfigManager.get("base.url");
        private static String managementToken = ConfigManager.get("management.token");

        // Static method to return a RequestSpecification instance
        public static RequestSpecification getRequestSpec() {
            if (baseUrl == null || managementToken == null) {
                throw new IllegalArgumentException("Configuration values for base URL or management token are missing.");
            }

            return new RequestSpecBuilder()
                    .setBaseUri(baseUrl)
                    .addHeader("Authorization", "Bearer " + managementToken)
                    .setContentType("application/json")
                    .build();
        }
}
