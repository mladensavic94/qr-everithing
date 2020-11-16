package io.github.mladensavic94;

import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

@Testcontainers
@QuarkusTest
class ImageResourceIT extends TestContainerSetup {

    @ConfigProperty(name = "content.folder.address")
    String contentFolder;

    @Test
    void testGetImage() throws Exception {
        byte[] result = given()
                .when()
                .header("Content-Type", "application/json")
                .get("/images/name.png")
                .then()
                .statusCode(200)
                .extract().asByteArray();
        assertEquals(result.length, new File(contentFolder+ "name.png").length());
    }

    @Test
    void testGetImageWithScale() throws Exception {
        byte[] result = given()
                .when()
                .header("Content-Type", "application/json")
                .queryParam("scale", "MEDIUM")
                .get("/images/name.png")
                .then()
                .statusCode(200)
                .extract().asByteArray();
        assertEquals(result.length, new File(contentFolder+ "name_medium.png").length());
    }

    @Test
    void testGetImageWithCustomScale() throws Exception {
        byte[] result = given()
                .when()
                .header("Content-Type", "application/json")
                .queryParam("scale", "CUSTOM")
                .queryParam("size", 100)
                .get("/images/name.png")
                .then()
                .statusCode(200)
                .extract().asByteArray();
        assertEquals(result.length, new File(contentFolder+ "name_custom.png").length());
    }

}
