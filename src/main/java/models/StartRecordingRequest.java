package models;

public class StartRecordingRequest {
    private String room_id;

    public StartRecordingRequest(String roomId) {
        this.room_id = roomId;
    }

    public String getRoomId() {
        return room_id;
    }

    public void setRoomId(String roomId) {
        this.room_id = roomId;
    }

    @Override
    public String toString() {
        return "{ \"room_id\": \"" + room_id + "\" }";
    }
}
