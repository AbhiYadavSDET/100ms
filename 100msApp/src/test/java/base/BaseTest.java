package base;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import utils.ConfigManager;

public class BaseTest {


    @BeforeClass
    public void setup() {
        // Set the base URI for RestAssured
        RestAssured.baseURI = ConfigManager.get("base.url");

        // Optionally, set default headers if needed
        RestAssured.given()
                .header("Authorization", getAuthToken())
                .header("Content-Type", "application/json");
    }

    protected String getAuthToken() {
        return ConfigManager.get("auth.token");

}
}
