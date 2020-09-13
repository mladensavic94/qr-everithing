package io.github.mladensavic94;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class ExampleResourceTest {

    @ClassRule
    public static PostgresContainer container = PostgresContainer.getInstance();

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/qr")
          .then()
             .statusCode(200)
             .body(is(notNullValue()));
    }

}