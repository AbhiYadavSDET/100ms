package tests;

import base.BaseTest;
import helpers.RecordingHelper;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.ConfigManager;
import utils.ResponseValidator;
import com.aventstack.extentreports.ExtentTest;
import utils.ExtentReporter;

public class RecordingAPITests extends BaseTest {
    private RecordingHelper recordingHelper;
    private String roomId;
    private String meetingUrl;
    private ExtentTest test;

    @BeforeClass
    public void setup() {
        recordingHelper = new RecordingHelper();
        roomId = ConfigManager.get("room.id");
        meetingUrl = ConfigManager.get("meeting.url"); // Get meeting URL from configuration
        Assert.assertNotNull(roomId, "Room ID must be set in the configuration file.");
        Assert.assertNotNull(meetingUrl, "Meeting URL must be set in the configuration file.");
    }

    @Test(priority = 1)
    public void testStartRecording() {
        // Start the test in ExtentReport
        test = ExtentReporter.getReporter().createTest("Start Recording Test for Room ID: " + roomId);

        test.info("Starting recording for Room ID: " + roomId);
        Response response = recordingHelper.startRecording(roomId, meetingUrl);

        test.info("Response Status Code: " + response.getStatusCode());

        // Validate the response
        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateField(response, "status", "starting");

        if (response.statusCode() == 200) {
            test.pass("Recording started successfully.");
        } else {
            test.fail("Failed to start recording. Status Code: " + response.statusCode());
        }
    }

    @Test(priority = 2, dependsOnMethods = "testStartRecording")
    public void testStartRecordingDuplicate() {
        // Start the test in ExtentReport
        test = ExtentReporter.getReporter().createTest("Start Recording Duplicate Test for Room ID: " + roomId);

        test.info("Attempting to start recording again for Room ID: " + roomId);
        Response response = recordingHelper.startRecording(roomId, meetingUrl);

        test.info("Response Status Code: " + response.getStatusCode());

        // Validate the response
        ResponseValidator.validateStatusCode(response, 409);
        ResponseValidator.validateField(response, "message", "beam already started");

        if (response.statusCode() == 409) {
            test.pass("Recording start failed as expected: Beam already started.");
        } else {
            test.fail("Unexpected response. Status Code: " + response.statusCode());
        }
    }

    @Test(priority = 3, dependsOnMethods = "testStartRecording")
    public void testStopRecording() {
        // Start the test in ExtentReport
        test = ExtentReporter.getReporter().createTest("Stop Recording Test for Room ID: " + roomId);

        test.info("Stopping recording for Room ID: " + roomId);
        Response response = recordingHelper.stopRecording(roomId, meetingUrl);

        test.info("Response Status Code: " + response.getStatusCode());

        // Validate the response status code
        ResponseValidator.validateStatusCode(response, 200);

        // Extract data from the response
        String status = response.jsonPath().getString("data[0].status");
        String meetingUrlFromResponse = response.jsonPath().getString("data[0].meeting_url");

        // Assertions for status and meeting URL
        Assert.assertEquals(status, "stopping", "Recording status is not stopping.");
        Assert.assertEquals(meetingUrlFromResponse, meetingUrl, "Meeting URL in response does not match the provided URL.");

        test.pass("Recording stopped successfully with status: " + status);
    }

    @Test(priority = 4)
    public void testStopRecordingWithoutActiveRecording() {
        // Start the test in ExtentReport
        test = ExtentReporter.getReporter().createTest("Stop Recording Without Active Recording Test for Room ID: " + roomId);

        test.info("Attempting to stop a recording when none is active.");
        Response response = recordingHelper.stopRecording(roomId, meetingUrl);

        test.info("Response Status Code: " + response.getStatusCode());

        // Validate the response
        ResponseValidator.validateStatusCode(response, 404);

        if (response.statusCode() == 404) {
            test.pass("Stop recording request failed as expected, no active recording found.");
        } else {
            test.fail("Unexpected response. Status Code: " + response.statusCode());
        }
    }
}
