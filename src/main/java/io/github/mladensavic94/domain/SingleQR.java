package io.github.mladensavic94.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "single_qr", schema = "public")
public class SingleQR {

    @Id
    @SequenceGenerator(name = "qrSequence", sequenceName = "qrSequence", allocationSize = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qrSequence")
    private Long id;
    @NotBlank(message = "Name cannot be null")
    private String name;
    @NotBlank(message = "QR link cannot be null")
    private String qrLink;
    private String qrCodeImgLink;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQrLink() {
        return qrLink;
    }

    public void setQrLink(String link) {
        this.qrLink = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQrCodeImgLink() {
        return qrCodeImgLink;
    }

    public void setQrCodeImgLink(String qrCodeImgLink) {
        this.qrCodeImgLink = qrCodeImgLink;
    }
}
