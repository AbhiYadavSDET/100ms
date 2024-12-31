package models;

public class RecordingRequest {

    private String meeting_url; // Correctly named field

    // Constructor to initialize the meeting_url
    public RecordingRequest(String meetingUrl) {
        this.meeting_url = meetingUrl;
    }

    // Getter for meeting_url
    public String getMeeting_url() {
        return meeting_url;
    }

    // Setter for meeting_url
    public void setMeeting_url(String meetingUrl) {
        this.meeting_url = meetingUrl;
    }

    // Override toString method to return the correct JSON structure
    @Override
    public String toString() {
        return "{ \"meeting_url\": \"" + meeting_url + "\" }"; // Correct JSON structure
    }
}
