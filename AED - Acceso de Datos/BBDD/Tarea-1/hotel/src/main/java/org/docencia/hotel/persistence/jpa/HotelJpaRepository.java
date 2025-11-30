package org.docencia.hotel.persistence.jpa;



import org.docencia.hotel.domain.repository.HotelRepository;
import org.docencia.hotel.model.Hotel;




public class HotelJpaRepository extends AbstractJpaRepository<Hotel,String> implements HotelRepository {


    protected HotelJpaRepository(Class<Hotel> entityClass) {
        super(entityClass);

    }

}
