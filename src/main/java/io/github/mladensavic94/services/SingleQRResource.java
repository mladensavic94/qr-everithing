package io.github.mladensavic94.services;

import io.github.mladensavic94.domain.QREverythingException;
import io.github.mladensavic94.domain.SingleQR;
import io.github.mladensavic94.repositories.SingleQRRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
@Path("/qr")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SingleQRResource {

    @Inject
    SingleQRRepository repo;

    @GET
    public Multi<SingleQR> get() {
        return repo.findAll();
    }

    @GET
    @Path("/{id}")
    public Uni<SingleQR> get(@PathParam Long id) {
        return repo.findById(id);
    }

    @POST
    public Uni<Response> addNew(SingleQR singleQR) {
        Uni<Long> id = repo.persist(singleQR);
        return id.onItem().transform(aLong -> Response.ok(aLong).build())
                .onFailure().transform(throwable -> new QREverythingException("Error persisting qr code: %s", throwable.getMessage()));
    }
}