package io.github.mladensavic94.qrcode;

import io.github.mladensavic94.domain.QREverythingException;
import io.github.mladensavic94.domain.SingleQR;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.imageio.ImageIO;
import javax.inject.Singleton;
import java.io.File;

@Singleton
public class QRCodeGenerator {

    private static final Logger LOGGER = Logger.getLogger(QRCodeGenerator.class.getName());

    @ConfigProperty(name = "content.folder.address")
    String contentFolder;
    @ConfigProperty(name = "content.host.address")
    String contentWebPath;

    QRCodeGeneratorService generator;
    public static final String IMAGE_EXTENSION = ".png";

    public QRCodeGenerator(QRCodeGeneratorService generator) {
        this.generator = generator;
    }

    public void generateQRCodeImage(SingleQR singleQR) {
        LOGGER.info("Generating qr code " + singleQR.getName());
        try {
            var bufferedImage = generator.createQRCode(singleQR);
            var fileName = prepareFileName(singleQR.getName());
            createContentFolder();
            ImageIO.write(bufferedImage, "png", new File(contentFolder + fileName));
            singleQR.setQrCodeImgLink(contentWebPath + fileName);
        } catch (Exception e) {
            LOGGER.error("IO error", e);
            throw new QREverythingException("Error generation QR code: %s", e.getMessage());
        }
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
