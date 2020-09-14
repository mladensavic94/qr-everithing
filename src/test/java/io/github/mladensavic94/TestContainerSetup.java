package io.github.mladensavic94;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class TestContainerSetup {

    @Container
    static PostgreSQLContainer db = new PostgreSQLContainer<>("postgres:10.5")
            .withExposedPorts(5432)
            .withInitScript("init.sql")
            .withCreateContainerCmdModifier(cmd -> {
                cmd
                        .withHostName("localhost")
                        .withPortBindings(new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432)));
            });

}
