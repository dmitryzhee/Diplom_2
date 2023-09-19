import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

public class UserLoginTest implements TestData {

  private BurgersClient client = new BurgersClient();

  private RequestSpecification requestSpecification;

  private ValidatableResponse response;

  private User user;

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
  public void existingUserLoginSuccess() {
    user = USER;
    client.createUser(user);
    ValidatableResponse response = client.login(user);
    response.assertThat().statusCode(200).body("success", is(true));
  }

  @Test
  public void invalidPasswordLoginFailure() {
    user = USER;
    client.createUser(user);
    String oldPassword = user.getPassword();
    user.setPassword("invalid");
    response = client.login(user);
    response.assertThat().statusCode(401).body(containsString("email or password are incorrect"));
    user.setPassword(oldPassword);
  }

  @Test
  public void invalidEmailLoginFailure() {
    user = USER;
    client.createUser(user);
    String oldEmail = user.getEmail();
    user.setEmail("invalid");
    response = client.login(user);
    response.assertThat().statusCode(401).body(containsString("email or password are incorrect"));
    user.setEmail(oldEmail);
  }



}
