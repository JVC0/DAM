package org.docencia.hotel.domain.repository;

import java.util.*;

import org.docencia.hotel.model.Hotel;


public interface HotelRepository {
    boolean existsById(String id);
    Optional<Hotel> findById(String id);
    List<Hotel> findAll();
    Hotel save(Hotel Hotel);
    boolean deleteById(String HotelId);
}
