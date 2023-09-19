import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;

public class UserCreationTest implements TestData{

    private BurgersClient client = new BurgersClient();

    private RequestSpecification requestSpecification;


    @Before
    public void setUp() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .build();
        client.setRequestSpecification(requestSpecification);
        }

    @Test
    public void userCreationSuccess() {
        User user = User.builder().email("test-burger1@test.com").password("12345").name("John").build();
        ValidatableResponse response = client.createUser(user);
        response.assertThat().statusCode(200);

    }


    }


