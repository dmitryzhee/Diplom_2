import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class UserEditTest implements TestData{

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
  public void authorizedUserNameChangeSuccess() {
    user = USER;
    client.createUser(user);
    Authorization authorization = client.login(user).extract().as(Authorization.class);
    user.setName("EditedName");
    ValidatableResponse response = client.editUser(authorization.getAccessToken(), user);
    response.assertThat().body("user.name", is("EditedName"));
  }

  @Test
  public void authorizedUserEmailChangeSuccess() {
    user = USER;
    client.createUser(user);
    Authorization authorization = client.login(user).extract().as(Authorization.class);
    user.setEmail("editedemail@burger.test");
    ValidatableResponse response = client.editUser(authorization.getAccessToken(), user);
    response.assertThat().body("user.email", is("editedemail@burger.test"));
  }

  @Test
  public void authorizedUserPasswordChangeSuccess() {
    user = USER;
    client.createUser(user);
    Authorization authorization = client.login(user).extract().as(Authorization.class);
    user.setPassword("editedPassword123!");
    client.editUser(authorization.getAccessToken(), user);
    client.logout(authorization.getRefreshToken());
    ValidatableResponse response = client.login(user);
    response.assertThat().statusCode(200).body("accessToken", notNullValue());

  }


}
