package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReporterListener implements ITestListener {
    private ExtentReports extent;
    private ExtentSparkReporter sparkReporter;

    @Override
    public void onStart(ITestContext context) {
        // Initialize ExtentReports before the suite starts
        String reportFilePath = "test-output/extent-report.html"; // File path for the report
        sparkReporter = new ExtentSparkReporter(reportFilePath);
        sparkReporter.config().setReportName("API Test Report");
        sparkReporter.config().setDocumentTitle("Test Results");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    @Override
    public void onFinish(ITestContext context) {
        // Flush the reports after all tests are finished
        extent.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Create a new test in the report
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        ExtentReporter.setTest(test);  // Save the current test in a ThreadLocal
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Mark the test as passed
        ExtentReporter.getTest().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Mark the test as failed
        ExtentReporter.getTest().fail("Test failed: " + result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Mark the test as skipped
        ExtentReporter.getTest().skip("Test skipped");
    }
}
