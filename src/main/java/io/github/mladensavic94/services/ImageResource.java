package io.github.mladensavic94.services;

import io.github.mladensavic94.domain.ImageScale;
import io.github.mladensavic94.repositories.ImageRepository;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Singleton
@Path("/images")
public class ImageResource {

    private static final Logger LOGGER = Logger.getLogger(ImageResource.class.getName());

    @Inject
    ImageRepository imageRepository;

    @GET
    @Path("{imageName}")
    @Produces("image/png")
    public Uni<Response> returnImage(@PathParam String imageName, @QueryParam("scale") @DefaultValue("SMALL") String scale,
                                     @QueryParam("size") @DefaultValue("200") int size) {
        File img = imageRepository.retrieveImage(imageName, ImageScale.valueOf(scale.toUpperCase()), size);
        LOGGER.info("Retrieved image " + img.getPath());
        return Uni.createFrom().item(Response.ok(img).build());
    }
}
