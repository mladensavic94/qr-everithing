package io.github.mladensavic94.qrcode;

import io.github.mladensavic94.domain.SingleQR;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.imageio.ImageIO;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;

@Singleton
public class QRCodeGenerator {

    @ConfigProperty(name = "content.folder.address")
    String contentFolder;

    QRCodeGeneratorService generator;
    public static final String IMAGE_EXTENSION = ".png";

    public QRCodeGenerator(QRCodeGeneratorService generator) {
        this.generator = generator;
    }

    private static final Logger LOGGER = Logger.getLogger(QRCodeGenerator.class.getName());

    public void generateQRCodeImage(SingleQR singleQR, String contentWebPath) {
        LOGGER.info("Generating qr code");
        var bufferedImage = generator.createQRCode(singleQR);
        var fileName = prepareFileName(singleQR.getName());
        try {
            createContentFolder();
            ImageIO.write(bufferedImage, "png", new File(contentFolder + fileName));
        } catch (IOException e) {
            LOGGER.error("IO error", e);
        }
        singleQR.setQrCodeImgLink(contentWebPath + fileName);
    }

    private String prepareFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9.-]", "_") + IMAGE_EXTENSION;
    }

    private void createContentFolder() {
        var root = new File(contentFolder);
        if (!root.exists()) {
            if (root.mkdir())
                LOGGER.error(contentFolder + " does not exist, created!");
            else
                LOGGER.error(contentFolder + " does not exist, can not be created!");

        }
    }

}
