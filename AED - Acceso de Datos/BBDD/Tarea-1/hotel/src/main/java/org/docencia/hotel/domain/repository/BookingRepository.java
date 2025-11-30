package org.docencia.hotel.domain.repository;

import java.util.*;

import org.docencia.hotel.model.Booking;


public interface BookingRepository {
    boolean existsById(String id);
    Optional<Booking> findById(String id);
    List<Booking> findAll();
    Booking save(Booking Booking);
    boolean deleteById(String BookingId);
    List<Booking> allBookings(String id,String checkInDate,String checkOutDate);
}
