package tests;

import base.BaseTest;
import helpers.RecordingHelper;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.ConfigManager;
import com.aventstack.extentreports.ExtentTest;
import utils.ExtentReporter;

import java.io.File;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

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

        try {
            if (response.statusCode() == 200) {
                test.pass("Recording started successfully.");
            } else {
                test.fail("Failed to start recording. Status Code: " + response.statusCode());
            }

            // Validate response schema
            test.info("Validating JSON schema for Start API response.");
            response.then().assertThat()
                    .body(matchesJsonSchema(new File("src/test/resources/schemas/startRecordingSchema.json")));

            Assert.assertEquals(response.jsonPath().getString("status"), "starting", "Recording status should be 'starting'");

        } catch (AssertionError e) {
            test.fail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 2, dependsOnMethods = "testStartRecording")
    public void testStartRecordingDuplicate() {
        // Start the test in ExtentReport
        test = ExtentReporter.getReporter().createTest("Start Recording Duplicate Test for Room ID: " + roomId);

        test.info("Attempting to start recording again for Room ID: " + roomId);
        Response response = recordingHelper.startRecording(roomId, meetingUrl);

        test.info("Response Status Code: " + response.getStatusCode());

        try {
            // Validate the response
            Assert.assertEquals(response.statusCode(), 409, "Status Code should be 409.");
            Assert.assertEquals(response.jsonPath().getString("message"), "beam already started", "Error message should indicate beam already started.");

            if (response.statusCode() == 409) {
                test.pass("Recording start failed as expected: Beam already started.");
            } else {
                test.fail("Unexpected response. Status Code: " + response.statusCode());
            }
        } catch (AssertionError e) {
            test.fail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 3, dependsOnMethods = "testStartRecording")
    public void testStopRecording() {
        test = ExtentReporter.getReporter().createTest("Stop Recording Test for Room ID: " + roomId);

        // Debugging: Log essential variables
        test.info("Room ID: " + roomId);
        test.info("Meeting URL: " + meetingUrl);

        if (roomId == null || meetingUrl == null) {
            test.skip("Test skipped: roomId or meetingUrl is null.");
            throw new SkipException("roomId or meetingUrl is null.");
        }

        Response response = recordingHelper.stopRecording(roomId, meetingUrl);

        test.info("Response Status Code: " + response.getStatusCode());
        test.info("Response Body: " + response.asPrettyString());

        try {
            Assert.assertEquals(response.statusCode(), 200, "Expected HTTP status code 200.");
          /*  response.then().assertThat()
                    .body(matchesJsonSchema(new File("src/test/resources/schemas/stopRecordingSchema.json")));*/

            String status = response.jsonPath().getString("data[0].status");
            Assert.assertEquals(status, "stopping", "Recording status is not 'stopping'.");

            test.pass("Recording stopped successfully.");
        } catch (Exception e) {
            test.fail("Test failed: " + e.getMessage());
            throw e;
        }
    }


    @Test(priority = 4)
    public void testStopRecordingWithoutActiveRecording() {
        // Start the test in ExtentReport
        test = ExtentReporter.getReporter().createTest("Stop Recording Without Active Recording Test for Room ID: " + roomId);

        test.info("Attempting to stop a recording when none is active.");
        Response response = recordingHelper.stopRecording(roomId, meetingUrl);

        test.info("Response Status Code: " + response.getStatusCode());

        try {
            // Validate the response
            Assert.assertEquals(response.statusCode(), 404, "Status Code should be 404.");

            if (response.statusCode() == 404) {
                test.pass("Stop recording request failed as expected, no active recording found.");
            } else {
                test.fail("Unexpected response. Status Code: " + response.statusCode());
            }
        } catch (AssertionError e) {
            test.fail("Test failed: " + e.getMessage());
            throw e;
        }
    }
}
