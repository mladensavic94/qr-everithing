package io.github.mladensavic94;

import io.github.mladensavic94.domain.ImageScale;
import io.github.mladensavic94.domain.QRCodeType;
import io.github.mladensavic94.domain.QREverythingException;
import io.github.mladensavic94.domain.SingleQR;
import io.github.mladensavic94.qrcode.QRCodeGeneratorService;
import io.github.mladensavic94.qrcode.ZxingQRCodeImpl;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static io.github.mladensavic94.TestsUtil.prepareDummyQR;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Mladen Savic (mladensavic94@gmail.com)
 */
@QuarkusTest
class QRCodeGeneratorServiceTest {

    QRCodeGeneratorService qrCodeGeneratorService = new ZxingQRCodeImpl();

    @Test
    void testPrepareQRContentForWeb(){
        SingleQR singleQR = prepareDummyQR();
        singleQR.setType(QRCodeType.WEB);

        String response = qrCodeGeneratorService.prepareQRContent(singleQR);

        assertThat(response, equalTo(singleQR.getQrLink()));
    }

    @Test
    void testPrepareQRContentForIG(){
        SingleQR singleQR = prepareDummyQR();
        singleQR.setType(QRCodeType.INSTAGRAM);

        String response = qrCodeGeneratorService.prepareQRContent(singleQR);

        assertThat(response, equalTo("instagram://"+singleQR.getQrLink()));
    }

    @Test
    void testPrepareQRContentForFB(){
        SingleQR singleQR = prepareDummyQR();
        singleQR.setType(QRCodeType.FACEBOOK);

        String response = qrCodeGeneratorService.prepareQRContent(singleQR);

        assertThat(response, equalTo("fb://"+singleQR.getQrLink()));
    }

    @Test
    void testPrepareQRContentForTw(){
        SingleQR singleQR = prepareDummyQR();
        singleQR.setType(QRCodeType.TWITTER);

        String response = qrCodeGeneratorService.prepareQRContent(singleQR);

        assertThat(response, equalTo(null));
    }
    @Test
    void testPrepareQRContentForWifi(){
        SingleQR singleQR = prepareDummyQR();
        singleQR.setType(QRCodeType.WIFI);

        String response = qrCodeGeneratorService.prepareQRContent(singleQR);

        assertThat(response, equalTo(""));
    }

    @Test
    void testQrCodeCreation(){
        SingleQR singleQR = prepareDummyQR();
        singleQR.setName("differentName");
        BufferedImage qrCode = qrCodeGeneratorService.createQRCode(singleQR);

        assertThat(qrCode, is(not(nullValue())));
        assertThat(qrCode.getHeight(), equalTo(ImageScale.SMALL.getSize()));
        assertThat(qrCode.getWidth(), equalTo(ImageScale.SMALL.getSize()));
    }

    @Test
    void testQrCodeCreationFails(){
        SingleQR singleQR = prepareDummyQR();
        singleQR.setType(null);
        singleQR.setName("differentName");

        Assertions.assertThrows(QREverythingException.class,
                () -> qrCodeGeneratorService.createQRCode(singleQR));
    }

}
