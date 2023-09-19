import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

public class UserCreationTest implements TestData{

    private BurgersClient client = new BurgersClient();

    private RequestSpecification requestSpecification;

    User user;


    @Before
    public void setUp() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .build();
        client.setRequestSpecification(requestSpecification);
        }

    @After
    public void tearDown() {
       if (user.equals(USER)) {
           Authorization authorization = client.login(user).extract().as(Authorization.class);
           client.deleteUser(authorization.getAccessToken());}
    }

    @Test
    public void userCreationSuccess() {
        user = USER;
        ValidatableResponse response = client.createUser(user);
        response.assertThat().statusCode(200).body("success", is(true));
    }

    @Test
    public void userCreationWithRegisteredCredentialsFailure() {
       user = USER;
       client.createUser(user);
       ValidatableResponse response = client.createUser(user);
       response.assertThat().statusCode(403).body(containsString("User already exists"));
    }

    @Test
    public void userCreationWithoutEmailFailure() {
        user = NO_EMAIL_USER;
        client.createUser(user);
        ValidatableResponse response = client.createUser(user);
        response.assertThat().statusCode(403).body(containsString("Email, password and name are required fields"));
    }

    @Test
    public void userCreationWithoutPasswordFailure() {
        user = NO_PASSWORD_USER;
        client.createUser(user);
        ValidatableResponse response = client.createUser(user);
        response.assertThat().statusCode(403).body(containsString("Email, password and name are required fields"));
    }

    @Test
    public void userCreationWithoutNameFailure() {
        user = NO_NAME_USER;
        client.createUser(user);
        ValidatableResponse response = client.createUser(user);
        response.assertThat().statusCode(403).body(containsString("Email, password and name are required fields"));
    }


    }


