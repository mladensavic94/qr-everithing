package io.github.mladensavic94;

import io.github.mladensavic94.domain.SingleQR;
import io.github.mladensavic94.repositories.SingleQRRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;

@Testcontainers
@QuarkusTest
class SingleQRResourceIT extends TestContainerSetup {

    @Inject
    SingleQRRepository repository;

    @Test
    void testCreateNewRecord() {
        SingleQR singleQR = prepareDummyQR();
        given()
                .when()
                .body(singleQR)
                .header("Content-Type", "application/json")
                .post("/qr")
                .then()
                .statusCode(200)
                .body(is(not(equalTo(0))));
    }

    @Test
    void testCreateNewRecordWithoutName() {
        SingleQR singleQR = new SingleQR();
        given()
                .when()
                .body(singleQR)
                .header("Content-Type", "application/json")
                .post("/qr")
                .then()
                .statusCode(500);
    }

    @Test
    void testGetQRById() throws Exception {
        SingleQR singleQR = prepareDummyQR();
        Long id = repository.persist(singleQR).subscribe().asCompletionStage().get();
        Response response = given()
                .when()
                .header("Content-Type", "application/json")
                .get("/qr/{id}", id.toString())
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON).extract().response();
        assertEquals("http://localhost:8080/images/name.png", response.jsonPath().getString("qrCodeImgLink"));
        assertEquals(response.jsonPath().getString("description"), singleQR.getDescription());
        assertEquals(response.jsonPath().getString("name"), singleQR.getName());

    }

    @Test
    void testGetAll() throws Exception {
        SingleQR singleQR = prepareDummyQR();
        Long id = repository.persist(singleQR).subscribe().asCompletionStage().get();
        Response response = given()
                .when()
                .header("Content-Type", "application/json")
                .get("/qr")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON).extract().response();
        System.out.println(response.jsonPath().prettyPrint());
        assertEquals("[qrImgLink]", response.jsonPath().getString("qrCodeImgLink"));
        assertEquals(response.jsonPath().getString("description"), "[" + singleQR.getDescription() + "]");
        assertEquals(response.jsonPath().getString("name"), "[" + singleQR.getName() + "]");

    }

    private SingleQR prepareDummyQR() {
        SingleQR singleQR = new SingleQR();
        singleQR.setQrLink("qrLink");
        singleQR.setQrCodeImgLink("qrImgLink");
        singleQR.setDescription("desc");
        singleQR.setName("name");
        return singleQR;
    }

}