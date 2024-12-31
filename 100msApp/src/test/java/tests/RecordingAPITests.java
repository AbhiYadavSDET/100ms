package tests;

import base.BaseTest;
import helpers.RecordingHelper;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.ConfigManager;
import utils.ResponseValidator;

public class RecordingAPITests extends BaseTest {
    private RecordingHelper recordingHelper;
    private String roomId;
    private String meetingUrl;

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
        System.out.println("Starting recording for Room ID: " + roomId);
        Response response = recordingHelper.startRecording(roomId, meetingUrl);

        System.out.println("Response Status Code: " + response.getStatusCode());

        // Validate the response
        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateField(response, "status", "starting");
    }

    @Test(priority = 2, dependsOnMethods = "testStartRecording")
    public void testStartRecordingDuplicate() {
        System.out.println("Attempting to start recording again for Room ID: " + roomId);
        Response response = recordingHelper.startRecording(roomId, meetingUrl);

        System.out.println("Response Status Code: " + response.getStatusCode());

        // Validate the response
        ResponseValidator.validateStatusCode(response, 409);
        ResponseValidator.validateField(response, "message", "beam already started");  // Updated to match the message field
    }

    @Test(priority = 3, dependsOnMethods = "testStartRecording")
    public void testStopRecording() {
        System.out.println("Stopping recording for Room ID: " + roomId);
        Response response = recordingHelper.stopRecording(roomId, meetingUrl);

        System.out.println("Response Status Code: " + response.getStatusCode());

        // Validate the response status code
        ResponseValidator.validateStatusCode(response, 200);

        // Extract data from the response
        String status = response.jsonPath().getString("data[0].status");
        String meetingUrlFromResponse = response.jsonPath().getString("data[0].meeting_url");

        // Assertions for status and stopped_at
        Assert.assertEquals(status, "stopping", "Recording status is not stopping.");
        Assert.assertEquals(meetingUrlFromResponse, meetingUrl, "Meeting URL in response does not match the provided URL.");
    }


    @Test(priority = 4)
    public void testStopRecordingWithoutActiveRecording() {
        System.out.println("Attempting to stop a recording when none is active.");
        Response response = recordingHelper.stopRecording(roomId,meetingUrl);

        System.out.println("Response Status Code: " + response.getStatusCode());

        // Validate the response
        ResponseValidator.validateStatusCode(response, 404);
    }
}
