package io.github.mladensavic94.repositories;

import io.github.mladensavic94.domain.ImageScale;
import io.github.mladensavic94.domain.QREverythingException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.imageio.ImageIO;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static io.github.mladensavic94.qrcode.QRCodeGenerator.IMAGE_EXTENSION;

@Singleton
public class ImageRepository {

    private static final Logger LOGGER = Logger.getLogger(ImageRepository.class.getName());

    @ConfigProperty(name = "content.folder.address")
    String contentFolder;

    public File retrieveImage(String imageName, ImageScale scale, int size) {
        if (scale.equals(ImageScale.SMALL)) {
            LOGGER.info("Defaulting to original size");
            return new File(contentFolder + imageName);
        } else {
            var scaledImageName = imageName.replace(IMAGE_EXTENSION, "_" + scale.name().toLowerCase() + IMAGE_EXTENSION);
            var scaledImage = new File(contentFolder + scaledImageName);
            if (scaledImage.exists()) {
                LOGGER.info("Scaled image exists " + scaledImageName);
                return scaledImage;
            } else {
                LOGGER.info("Scaling image to " + scale.name());
                return scaleImage(imageName, scaledImage, resolveDimensions(scale, size));
            }
        }


    }

    private int resolveDimensions(ImageScale scale, int size) {
        if (scale.equals(ImageScale.CUSTOM))
            return size;
        return scale.getSize();
    }

    private File scaleImage(String imageName, File scaledImage, int size) {
        try {
            var originalImage = new File(contentFolder + imageName);
            if (originalImage.exists()) {
                var original = ImageIO.read(originalImage);
                var scaledImageBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                var scaledInstance = original.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                scaledImageBuffer.getGraphics().drawImage(scaledInstance, 0, 0, null);
                ImageIO.write(scaledImageBuffer, "png", scaledImage);
                return scaledImage;
            }
        } catch (IOException e) {
            throw new QREverythingException("Image %s not found! Error %s", imageName, e);
        }
        throw new QREverythingException("Image %s not found!", imageName);
    }
}
