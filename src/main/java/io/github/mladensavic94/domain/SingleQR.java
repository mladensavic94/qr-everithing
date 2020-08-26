package io.github.mladensavic94.domain;

import io.vertx.mutiny.sqlclient.Row;

import javax.persistence.*;

@Entity
@Table(name = "single_qr", schema = "public")
public class SingleQR {

    @Id
    @SequenceGenerator(name = "qrSequence", sequenceName = "qrSequence", allocationSize = 1, initialValue = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qrSequence")
    private Long id;
    private String name;
    private String link;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static SingleQR from(Row row) {
        SingleQR qr = new SingleQR();
        qr.setId(row.getLong("id"));
        qr.setLink(row.getString("link"));
        qr.setDescription(row.getString("description"));
        qr.setName(row.getString("name"));
        return qr;
    }
}
