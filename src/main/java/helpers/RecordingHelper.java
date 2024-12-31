package helpers;

import io.restassured.response.Response;
import models.RecordingRequest;
import utils.ConfigManager;

import static io.restassured.RestAssured.given;

public class RecordingHelper {
    private String baseUrl;
    private String managementToken;

    public RecordingHelper() {
        this.baseUrl = ConfigManager.get("base.url");
        this.managementToken = ConfigManager.get("management.token");

        if (this.baseUrl == null || this.managementToken == null) {
            throw new IllegalArgumentException("Configuration values for base URL or management token are missing.");
        }
    }

    // Method to start the recording
    public Response startRecording(String roomId, String meetingUrl) {
        RecordingRequest requestBody = new RecordingRequest(meetingUrl);

        Response response = given().log().all()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + managementToken)
                .contentType("application/json")
                .body(requestBody)
                .post("/v2/live-streams/room/" + roomId + "/start")
                .then().log().all().extract().response();

        return response;
    }

    // Method to stop the recording
    public Response stopRecording(String roomId, String meetingUrl) {
        RecordingRequest requestBody = new RecordingRequest(meetingUrl);

        Response response = given().log().all()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + managementToken)
                .contentType("application/json")
                .body(requestBody)
                .post("/v2/live-streams/room/" + roomId + "/stop")
                .then().log().all().extract().response();

        return response;
    }




}
