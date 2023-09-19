import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BurgersClient implements TestData {
    private RequestSpecification requestSpecification;

    public void setRequestSpecification(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    public ValidatableResponse createUser(User user) {
        return given()
                .spec(requestSpecification)
                .body(user)
                .post(USER_CREATION_ENDPOINT)
                .then()
                .log().all();
    }


}
