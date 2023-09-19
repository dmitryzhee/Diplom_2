import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        response.assertThat().statusCode(200);
    }

    @Test
    public void userCreationWithRegisteredCredentialsFailure() {
       user = USER;
       client.createUser(USER);
       ValidatableResponse response = client.createUser(USER);
       response.assertThat().statusCode(403);

    }



    }


