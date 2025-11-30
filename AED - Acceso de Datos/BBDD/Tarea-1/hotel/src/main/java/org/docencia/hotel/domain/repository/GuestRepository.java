package org.docencia.hotel.domain.repository;


import java.util.*;

import org.docencia.hotel.model.Guest;

public interface GuestRepository {
    boolean existsById(String id);
    Optional<Guest> findById(String id);
    List<Guest> findAll();
    Guest save(Guest Guest);
    boolean deleteById(String GuestId);
}
