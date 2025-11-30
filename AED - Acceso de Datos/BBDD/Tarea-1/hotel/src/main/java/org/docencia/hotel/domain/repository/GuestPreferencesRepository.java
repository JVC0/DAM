package org.docencia.hotel.domain.repository;

import java.util.Optional;

import org.docencia.hotel.model.nosql.GuestPreferences;

public interface GuestPreferencesRepository {
    Optional<GuestPreferences> findByGuestId(String guestId);
    GuestPreferences save(GuestPreferences prefs);
    void deleteByGuestId(String guestId);
}
