package io.github.mladensavic94.qrcode;

import io.github.mladensavic94.domain.SingleQR;

import java.awt.image.BufferedImage;

public interface QRCodeGeneratorService {

    BufferedImage createQRCode(SingleQR singleQR);
}
