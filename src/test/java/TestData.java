import java.util.Random;

public interface TestData {
    String BASE_URI = "https://stellarburgers.nomoreparties.site";

    String USER_CREATION_ENDPOINT = "/api/auth/register";

    String USER_DATA_ENDPOINT = "/api/auth/user";

    String LOGIN_ENDPOINT = "/api/auth/login";

    String ORDER_CREATION_ENDPOINT = "/api/orders";

    String INGREDIENTS_ENDPOINT = "/api/ingredients";

    Random random = new Random();

    User USER = User.builder().email("burger-test@test.com").password("12345").name("Alex").build();

    User RANDOM_USER = User.builder().email(String.format("test-burger%s@test.com", random.nextInt(10000))).password("12345").name("John").build();

    User NO_NAME_USER = User.builder().email("burger-test@test.com").name("Alex").build();

    User NO_PASSWORD_USER = User.builder().email("burger-test@test.com").password("12345").build();

    User NO_EMAIL_USER = User.builder().password("12345").name("Alex").build();

}
