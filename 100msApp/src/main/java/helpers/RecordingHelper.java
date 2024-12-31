package helpers;

import io.restassured.response.Response;
import models.RecordingRequest;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RecordingHelper {

    // Method to start the recording
    public Response startRecording(String roomId, String meetingUrl) {
        // Create a request body using the meeting URL
        RecordingRequest requestBody = new RecordingRequest(meetingUrl);

        // Get the reusable RequestSpecification
        RequestSpecification requestSpec = RequestSpec.getRequestSpec();

        // Send the POST request to start the recording
        Response response = given().log().all()
                .spec(requestSpec) // Use the shared RequestSpecification
                .body(requestBody)
                .post("/v2/live-streams/room/" + roomId + "/start?skip_preview=true")
                .then()
                .log().all()
                .extract().response();

        return response;
    }

    // Method to stop the recording
    public Response stopRecording(String roomId, String meetingUrl) {
        // Create a request body using the meeting URL
        RecordingRequest requestBody = new RecordingRequest(meetingUrl);

        // Get the reusable RequestSpecification
        RequestSpecification requestSpec = RequestSpec.getRequestSpec();

        // Send the POST request to stop the recording
        Response response = given().log().all()
                .spec(requestSpec) // Use the shared RequestSpecification
                .body(requestBody)
                .post("/v2/live-streams/room/" + roomId + "/stop?skip_preview=true")
                .then()
                .log().all()
                .extract().response();

        return response;
    }
}
