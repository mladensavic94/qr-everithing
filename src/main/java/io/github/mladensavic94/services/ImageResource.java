package io.github.mladensavic94.services;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.File;

@ApplicationScoped
@Path("/images")
public class ImageResource {

    private static final Logger LOGGER = Logger.getLogger(ImageResource.class.getName());

    @ConfigProperty(name = "content.folder.address")
    String contentFolder;

    @GET
    @Path("{imageName}")
    @Produces("image/png")
    public Uni<Response> returnImage(@PathParam String imageName) {
        File img = new File(contentFolder + imageName);
        LOGGER.info("Retrieving image " + img.getPath());
        Uni<Response> item = Uni.createFrom().item(Response.ok(img).build());
        return item;
    }
}
