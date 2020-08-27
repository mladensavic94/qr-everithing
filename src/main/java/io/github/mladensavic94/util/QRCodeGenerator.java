package io.github.mladensavic94.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.github.mladensavic94.domain.SingleQR;
import org.jboss.logging.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCodeGenerator {
    private static final Logger LOGGER = Logger.getLogger(QRCodeGenerator.class.getName());

    static String localPath = "./images/";

    public static void generateQRCodeImage(SingleQR singleQR, String contentWebPath) {
        LOGGER.info("Generating qr code");

        BufferedImage bufferedImage = createQRCode(singleQR);

        String fileName = singleQR.getName() + ".png";
        try {
            createContentFolder();
            ImageIO.write(bufferedImage, "png", new File(localPath + fileName));
        } catch (IOException e) {
            LOGGER.error("IO error", e);
        }
        singleQR.setQrCodeImgLink(contentWebPath + fileName);
    }

    private static void createContentFolder() {
        File root = new File(localPath);
        if (!root.exists() && root.mkdir())
            LOGGER.error(localPath + " does not exist, created!");
    }

    private static BufferedImage createQRCode(SingleQR singleQR) {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = barcodeWriter.encode(singleQR.getQrLink(), BarcodeFormat.QR_CODE, 200, 200);
        } catch (WriterException e) {
            LOGGER.error(String.format("Error while creating qr code %s %s", singleQR.getQrLink(), e.getMessage()));
            try {
                bitMatrix = barcodeWriter.encode("DUMMY", BarcodeFormat.QR_CODE, 200, 200);
            } catch (WriterException ignored) {
            }

        }
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
