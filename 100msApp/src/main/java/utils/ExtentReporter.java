package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.ExtentTest;

public class ExtentReporter {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // Initialize ExtentReports and setup the report file path
    public static ExtentReports getReporter() {
        if (extent == null) {
            // Define the report file path
            String reportFilePath = "test-output/extent-report.html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFilePath);
            sparkReporter.config().setReportName("API Test Report");
            sparkReporter.config().setDocumentTitle("Test Results");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
        }
        return extent;
    }

    // Set the test instance for reporting
    public static void setTest(ExtentTest testInstance) {
        test.set(testInstance);
    }

    // Get the current test instance
    public static ExtentTest getTest() {
        return test.get();
    }

    // Flush the report to write it
    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
