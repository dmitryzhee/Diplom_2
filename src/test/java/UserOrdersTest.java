import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

public class UserOrdersTest implements TestData{

  private BurgersClient client = new BurgersClient();

  private RequestSpecification requestSpecification;

  private List<IngredientsData> ingredients;

  Random random;

  private User user;

  @Before
  public void setUp() {
    requestSpecification = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setContentType(ContentType.JSON)
            .build();
    client.setRequestSpecification(requestSpecification);
    ingredients =  client.getIngredients().extract().body().as(Ingredients.class).getData();
    random = new Random();
  }

  @After
  public void tearDown() {
    if (user!= null && user.equals(USER)) {
      Authorization authorization = client.login(user).extract().as(Authorization.class);
      client.deleteUser(authorization.getAccessToken());}
  }

  @Test
  public void getOrdersForAuthorizedUserSuccess() {
    Gson gson = new Gson();
    user = USER;
    client.createUser(user);
    Authorization authorization = client.login(user).extract().as(Authorization.class);
    String orderIngredient = ingredients.get(random.nextInt(ingredients.size())).get_id();
    OrderIngredients orderIngredients = new OrderIngredients(orderIngredient);
    String json = gson.toJson(orderIngredients);
    int burgerCount = random.nextInt(5) + 1;
    for (int i = 0; i < burgerCount; i++) {
      client.makeOrder(json, authorization.getAccessToken());
    }
    ValidatableResponse response = client.getUserOrders(authorization.getAccessToken());
    response.assertThat().statusCode(200);
    List<Order> orders = response.extract().as(Orders.class).getOrders();
    List<String> orderStatus = new ArrayList<>();
    for (int i = 0; i<orders.size(); i++) {
      orderStatus.add(orders.get(i).getStatus());
    }
    Assert.assertTrue(orderStatus.size() == burgerCount);
  }

  @Test
  public void getOrdersUnauthorizedUserFailure() {
    ValidatableResponse response = client.getUserOrders("");
    response.assertThat().statusCode(401).body(containsString("You should be authorised"));
  }





}
