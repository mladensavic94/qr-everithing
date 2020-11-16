package io.github.mladensavic94.qrcode;

import io.github.mladensavic94.domain.QRCodeType;
import io.github.mladensavic94.domain.SingleQR;

import java.awt.image.BufferedImage;

public interface QRCodeGeneratorService {

    BufferedImage createQRCode(SingleQR singleQR);

    default String prepareQRContent(SingleQR singleQR) {
        return switch (singleQR.getType()) {
            case WEB -> singleQR.getQrLink();
            case TWITTER -> null;
            case INSTAGRAM -> QRCodeType.INSTAGRAM.getDeepLinkShortCode() + singleQR.getQrLink();
            case WIFI -> "";
            case FACEBOOK -> QRCodeType.FACEBOOK.getDeepLinkShortCode() + singleQR.getQrLink();
        };
    }
}
