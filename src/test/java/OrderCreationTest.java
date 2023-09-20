import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class OrderCreationTest implements TestData{

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
  public void orderCreationSuccess() {
    List<IngredientsData> ingredients =  client.getIngredients().extract().body().as(Ingredients.class).getData();
    for (int i=0; i<ingredients.size();i++) {
      System.out.println(ingredients.get(i).get_id());
    }
  }
}
