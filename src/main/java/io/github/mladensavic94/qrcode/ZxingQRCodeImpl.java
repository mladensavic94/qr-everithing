package io.github.mladensavic94.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.github.mladensavic94.domain.ImageScale;
import io.github.mladensavic94.domain.QREverythingException;
import io.github.mladensavic94.domain.SingleQR;
import org.jboss.logging.Logger;

import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

@Singleton
public class ZxingQRCodeImpl implements QRCodeGeneratorService{

    private static final Logger LOGGER = Logger.getLogger(ZxingQRCodeImpl.class.getName());

    @Override
    public BufferedImage createQRCode(SingleQR singleQR) {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        try {
            Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);
            hintMap.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = barcodeWriter.encode(prepareQRContent(singleQR), BarcodeFormat.QR_CODE, ImageScale.SMALL.getSize(), ImageScale.SMALL.getSize(), hintMap);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (Exception e) {
            LOGGER.error(String.format("Error while creating qr code %s %s", singleQR.getQrLink(), e.getMessage()));
            throw new QREverythingException("Error in generation %s", e.getMessage());
        }
    }
}
