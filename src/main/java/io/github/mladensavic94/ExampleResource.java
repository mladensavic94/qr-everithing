package io.github.mladensavic94;

import io.github.mladensavic94.domain.SingleQR;
import io.github.mladensavic94.repositories.SingleQRRepo;
import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExampleResource {

    @Inject
    PgPool client;
    @Inject
    SingleQRRepo repo;

    @GET
    public Multi<SingleQR> hello() {
        System.out.println("aaa");
        return repo.findAll();
    }
}