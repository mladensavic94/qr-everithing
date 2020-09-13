package io.github.mladensavic94;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@Testcontainers
@QuarkusTest
class SingleQRResourceIT {

    @Container
    static PostgreSQLContainer db = new PostgreSQLContainer<>("postgres:10.5")
            .withExposedPorts(5432)
            .withInitScript("init.sql")
            .withCreateContainerCmdModifier(cmd -> {
                cmd
                        .withHostName("localhost")
                        .withPortBindings(new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432)));
            });


    @Test
    void testHelloEndpoint() {
        given()
                .when().get("/qr")
                .then()
                .statusCode(200)
                .body(is(notNullValue()));
    }

}