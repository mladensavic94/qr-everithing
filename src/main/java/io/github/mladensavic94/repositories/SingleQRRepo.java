package io.github.mladensavic94.repositories;

import io.github.mladensavic94.domain.SingleQR;
import io.github.mladensavic94.util.QRCodeGenerator;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SingleQRRepo {

    @Inject
    Mutiny.Session mutinySession;

    @ConfigProperty(name = "content.host.address")
    String contentWebPath;

    public Multi<SingleQR> findAll() {
        return mutinySession.createQuery("select q from SingleQR q", SingleQR.class).getResults();
    }

    public Uni<SingleQR> findById(Long id) {
        return mutinySession.find(SingleQR.class, id).onItem().transform(singleQR -> {
            QRCodeGenerator.generateQRCodeImage(singleQR, contentWebPath);
            return singleQR;
        });
    }

    public Uni<Long> persist(SingleQR singleQR) {
        return mutinySession.persist(singleQR)
                .onItem().transform(Mutiny.Session::flush)
                .onItem().transform(session -> singleQR.getId());
    }
}
