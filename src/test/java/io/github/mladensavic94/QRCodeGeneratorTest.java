package io.github.mladensavic94;

import io.github.mladensavic94.domain.QREverythingException;
import io.github.mladensavic94.domain.SingleQR;
import io.github.mladensavic94.qrcode.QRCodeGenerator;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

@QuarkusTest
public class QRCodeGeneratorTest {

    @ConfigProperty(name = "content.folder.address")
    String contentFolder;
    @ConfigProperty(name = "content.host.address")
    String contentWebPath;
    @Inject
    QRCodeGenerator qrCodeGenerator;

    @Test
    void generatorWillGenerateCorrectFile() {
        SingleQR singleQR = prepareDummyQR();
        qrCodeGenerator.generateQRCodeImage(singleQR);

        assertEquals(singleQR.getQrCodeImgLink(), contentWebPath+"QRCodeGeneratorTest.png");
        var qrCode = new File(contentFolder + "QRCodeGeneratorTest.png");
        assertTrue(qrCode.exists());
        assertThat(qrCode.length(), is(not(0)));
    }

    @Test
    void generatorWillGenerateCorrectFileWithForbiddenCharacters() {
        SingleQR singleQR = prepareDummyQR();
        singleQR.setName("inva!idName");
        qrCodeGenerator.generateQRCodeImage(singleQR);

        assertEquals(singleQR.getQrCodeImgLink(), contentWebPath+"inva_idName.png");
        var qrCode = new File(contentFolder + "inva_idName.png");
        assertTrue(qrCode.exists());
        assertThat(qrCode.length(), is(not(0)));
    }

    @Test
    void generatorWillThrowException() {
        SingleQR singleQR = new SingleQR();
        Assertions.assertThrows(QREverythingException.class, () -> qrCodeGenerator.generateQRCodeImage(singleQR));


    }

    private SingleQR prepareDummyQR() {
        SingleQR singleQR = new SingleQR();
        singleQR.setQrLink("qrLink");
        singleQR.setDescription("desc");
        singleQR.setName("QRCodeGeneratorTest");
        return singleQR;
    }
}
