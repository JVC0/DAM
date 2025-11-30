package org.docencia.hotel.persistence.jpa;


import org.docencia.hotel.domain.repository.GuestRepository;
import org.docencia.hotel.model.Guest;

public class GuestJpaRepository extends AbstractJpaRepository<Guest,String> implements GuestRepository {

    protected GuestJpaRepository(Class<Guest> entityClass) {
        super(entityClass);
    }

}
