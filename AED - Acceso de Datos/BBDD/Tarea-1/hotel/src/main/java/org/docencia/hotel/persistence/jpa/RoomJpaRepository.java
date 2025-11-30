package org.docencia.hotel.persistence.jpa;

import java.util.List;

import org.docencia.hotel.domain.repository.RoomRepository;
import org.docencia.hotel.model.Room;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

public class RoomJpaRepository extends AbstractJpaRepository<Room,String> implements RoomRepository {
    public final static String HOTELROOMS = "SELECT r FROM Room r WHERE r.hotel.id = :hotelId";
    protected RoomJpaRepository(Class<Room> entityClass) {
        super(entityClass);
       
    }


    @Override
    @Query(HOTELROOMS)
    public List<Room> getHotelRooms(@Param("hotelId") String hotelId) {
        return null;
    }

}
