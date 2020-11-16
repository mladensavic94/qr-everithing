package io.github.mladensavic94.domain;

public enum QRCodeType {

    WEB,
    FACEBOOK("fb://"),
    TWITTER,
    INSTAGRAM("instagram://"),
    WIFI;

    String deepLinkShortCode = "";

    QRCodeType(String deepLinkShortCode) {
        this.deepLinkShortCode = deepLinkShortCode;
    }

    QRCodeType() {
    }

    public String getDeepLinkShortCode() {
        return deepLinkShortCode;
    }
}
