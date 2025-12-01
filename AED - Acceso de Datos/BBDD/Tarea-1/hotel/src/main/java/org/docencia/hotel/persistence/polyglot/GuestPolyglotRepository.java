package org.docencia.hotel.persistence.polyglot;

import org.docencia.hotel.domain.repository.GuestRepository;
import org.docencia.hotel.domain.repository.GuestPreferencesRepository;
import org.docencia.hotel.model.Guest;
import org.docencia.hotel.model.nosql.GuestPreferences;
import org.docencia.hotel.persistence.jpa.GuestJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositorio polyglot que combina persistencia relacional (H2) y NoSQL (MongoDB).
 * 
 * Orquesta:
 * - GuestJpaRepository: para datos básicos del huésped en H2
 * - GuestPreferencesRepository: para preferencias en MongoDB
 * 
 * Devuelve objetos Guest completos con toda su información.
 */
@Repository
@Transactional
public class GuestPolyglotRepository implements GuestRepository {

    private final GuestJpaRepository guestJpaRepository;
    private final GuestPreferencesRepository preferencesRepository;

    public GuestPolyglotRepository(
            GuestJpaRepository guestJpaRepository,
            GuestPreferencesRepository preferencesRepository) {
        this.guestJpaRepository = guestJpaRepository;
        this.preferencesRepository = preferencesRepository;
    }

    @Override
    public boolean existsById(String id) {
        return guestJpaRepository.existsById(id);
    }

    @Override
    public Optional<Guest> findById(String id) {
        Optional<Guest> guestOpt = guestJpaRepository.findById(id);
        
        if (guestOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Guest guest = guestOpt.get();
        Optional<GuestPreferences> preferencesOpt = preferencesRepository.findByGuestId(id);
        preferencesOpt.ifPresent(guest::setPreferences);
        
        return Optional.of(guest);
    }

    @Override
    public List<Guest> findAll() {
        List<Guest> guests = guestJpaRepository.findAll();
        return guests.stream()
                .map(guest -> {
                    preferencesRepository.findByGuestId(guest.getId())
                            .ifPresent(guest::setPreferences);
                    return guest;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Guest save(Guest guest) {
        Guest savedGuest = guestJpaRepository.save(guest);
        if (guest.getPreferences() != null) {
            GuestPreferences preferences = guest.getPreferences();
            preferences.setGuestId(savedGuest.getId());
            GuestPreferences savedPreferences = preferencesRepository.save(preferences);
            savedGuest.setPreferences(savedPreferences);
        }
        
        return savedGuest;
    }

    @Override
    public boolean deleteById(String id) {
        preferencesRepository.deleteByGuestId(id);
        return true;
    }
}