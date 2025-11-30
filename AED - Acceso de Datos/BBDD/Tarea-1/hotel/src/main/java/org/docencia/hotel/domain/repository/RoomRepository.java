package org.docencia.hotel.domain.repository;

import java.util.*;

import org.docencia.hotel.model.Room;

public interface RoomRepository {
    boolean existsById(String id);
    Optional<Room> findById(String id);
    List<Room> findAll();
    Room save(Room room);
    boolean deleteById(String roomId);
    List<Room> getHotelRooms(String id);
}
