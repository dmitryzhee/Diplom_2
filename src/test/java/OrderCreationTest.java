import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.*;

public class OrderCreationTest implements TestData{

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
  public void unauthorizedUserOrderCreationSuccess() {
    Gson gson = new Gson();
    String orderIngredient = ingredients.get(random.nextInt(ingredients.size())).get_id();
    OrderIngredients orderIngredients = new OrderIngredients(orderIngredient);
    String json = gson.toJson(orderIngredients);
    ValidatableResponse response = client.makeOrder(json, "");
    response.assertThat().statusCode(200).body("order.number", notNullValue()); //создание работает без авторизации
  }

  @Test
  public void authorizedUserOrderCreationSuccess() {
    user = USER;
    Gson gson = new Gson();
    client.createUser(USER);
    Authorization authorization = client.login(user).extract().as(Authorization.class);
    String orderIngredient = ingredients.get(random.nextInt(ingredients.size())).get_id();
    OrderIngredients orderIngredients = new OrderIngredients(orderIngredient);
    String json = gson.toJson(orderIngredients);
    ValidatableResponse response = client.makeOrder(json, authorization.getAccessToken());
    response.assertThat().statusCode(200).body("order.owner.email", is(user.getEmail()));
  }


  @Test
  public void orderCreationNoIngredientsFailure() {
    ValidatableResponse response = client.makeOrder("", "");
    response.assertThat().statusCode(400).assertThat().body(containsString("Ingredient ids must be provided"));
  }

  @Test
  public void orderCreationInvalidIngredientsFailure() {
    Gson gson = new Gson();
    String orderIngredient = ingredients.get(random.nextInt(ingredients.size())).get_id() + "invalid";
    OrderIngredients orderIngredients = new OrderIngredients(orderIngredient);
    String json = gson.toJson(orderIngredients);
    ValidatableResponse response = client.makeOrder(json, "");
    response.assertThat().statusCode(500);
  }


}
