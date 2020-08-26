package io.github.mladensavic94.repositories;

import io.github.mladensavic94.domain.SingleQR;
import io.smallrye.mutiny.Multi;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SingleQRRepo {

    @Inject
    Mutiny.Session mutinySession;

    public Multi<SingleQR> findAll() {
        return mutinySession.createQuery("select q from SingleQR q", SingleQR.class).getResults();

    }
}
