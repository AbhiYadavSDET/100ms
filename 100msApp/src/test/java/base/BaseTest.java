package base;

import io.restassured.RestAssured;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import utils.ConfigManager;
import utils.ExtentReporter;

public class BaseTest {


    @BeforeSuite
    public void setupExtentReport() {
        // Initialize ExtentReports before any tests run
        ExtentReporter.getReporter();
    }
    @BeforeClass
    public void setup() {
        // Set the base URI for RestAssured
        RestAssured.baseURI = ConfigManager.get("base.url");

        // Optionally, set default headers if needed
        RestAssured.given()
                .header("Authorization", getAuthToken())
                .header("Content-Type", "application/json");
    }

    @AfterSuite
    public void tearDown() {
        // Flush Extent report after tests are complete
        ExtentReporter.flush();
    }

    protected String getAuthToken() {
        return ConfigManager.get("management.token");

}
}
