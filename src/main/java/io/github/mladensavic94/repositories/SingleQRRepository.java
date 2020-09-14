package io.github.mladensavic94.repositories;

import io.github.mladensavic94.domain.SingleQR;
import io.github.mladensavic94.qrcode.QRCodeGenerator;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;

@Singleton
public class SingleQRRepository {

    @Inject
    Mutiny.Session mutinySession;
    @Inject
    QRCodeGenerator generator;

    public Multi<SingleQR> findAll() {
        return mutinySession.createQuery("select q from SingleQR q", SingleQR.class).getResults();
    }

    public Uni<SingleQR> findById(Long id) {
        return mutinySession.find(SingleQR.class, id).onItem().transform(singleQR -> {
            generator.generateQRCodeImage(singleQR);
            return singleQR;
        });
    }

    public Uni<Long> persist(@Valid SingleQR singleQR) {
        return mutinySession.persist(singleQR)
                .onItem().transform(Mutiny.Session::flush)
                .onItem().transform(session -> singleQR.getId())
                .onFailure().transform(RuntimeException::new);
    }
}
