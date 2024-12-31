package utils;

import io.restassured.response.Response;
import org.testng.Assert;

public class ResponseValidator {

    public static void validateStatusCode(Response response, int expectedStatusCode) {
        Assert.assertEquals(response.statusCode(), expectedStatusCode,
                "Unexpected status code: " + response.asString());
    }

    public static void validateField(Response response, String jsonPath, String expectedValue) {
        Assert.assertEquals(response.jsonPath().getString(jsonPath), expectedValue,
                "Unexpected value for field: " + jsonPath);

}
}
