package org.docencia.hotel.persistence.jpa;
import java.util.List;
import org.docencia.hotel.domain.repository.BookingRepository;
import org.docencia.hotel.model.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public class BookingJpaRepository extends AbstractJpaRepository<Booking,String> implements BookingRepository {
    public final static String BOOKINGFORDATE = "SELECT b FROM Booking b WHERE b.room.id = :roomId AND b.fechaDeEntrada <= :endingDate AND b.fechaDeSalida >= :startingDate";
    protected BookingJpaRepository(Class<Booking> entityClass) {
        super(entityClass);
    }

    @Override
    @Query(BOOKINGFORDATE)
    public List<Booking> allBookings(@Param("roomId") String roomId,
            @Param("startingDate") String startingDate,
            @Param("endingDate") String endingDate) {
        return null;
    }

}
