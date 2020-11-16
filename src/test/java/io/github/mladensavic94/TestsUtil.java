package io.github.mladensavic94;

import io.github.mladensavic94.domain.SingleQR;

/**
 * @author Mladen Savic (mladensavic94@gmail.com)
 */
public class TestsUtil {

    public static SingleQR prepareDummyQR() {
        SingleQR singleQR = new SingleQR();
        singleQR.setQrLink("qrLink");
        singleQR.setQrCodeImgLink("qrImgLink");
        singleQR.setDescription("desc");
        singleQR.setName("name");
        return singleQR;
    }
}
