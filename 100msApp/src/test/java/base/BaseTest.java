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
    @AfterSuite
    public void tearDown() {
        // Flush Extent report after tests are complete
        ExtentReporter.flush();
    }
}
