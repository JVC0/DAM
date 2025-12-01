package org.docencia.hotel.domain.repository;

import java.util.Optional;

import org.docencia.hotel.model.nosql.GuestPreferences;

/**
 * Repositorio de acceso a las preferencias de huésped almacenadas en MongoDB.
 * <p>
 * Este repositorio trabaja con documentos {@link GuestPreferences} y permite
 * consultarlos y modificarlos a partir del identificador del huésped relacional.
 */
public interface GuestPreferencesRepository {


    Optional<GuestPreferences> findByGuestId(String guestId);


    GuestPreferences save(GuestPreferences prefs);

    void deleteByGuestId(String guestId);
}
