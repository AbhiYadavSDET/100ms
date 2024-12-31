package helpers;

import io.restassured.response.Response;
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
        Response response = given().log().all()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + managementToken) // Use management token
                .contentType("application/json")
                .body("{\"meeting_url\": \"" + meetingUrl + "\"}") // Updated request body
                .post("/v2/live-streams/room/" + roomId + "/start")
                .then().log().all().extract().response();

        return response;
    }

    public Response stopRecording(String roomId, String meetingUrl) {

        Response response = given().log().all()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + managementToken) // Use management token
                .contentType("application/json")
                .body("{ \"meeting_url\": \"" + meetingUrl + "\" }") // Pass the meeting URL in the request body
                .post("/v2/live-streams/room/" + roomId + "/stop")
                .then().log().all().extract().response();

        return response;
    }



}
