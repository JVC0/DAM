package org.docencia.hotel.persistence.nosql;

import org.docencia.hotel.domain.repository.GuestPreferencesRepository;
import org.docencia.hotel.model.nosql.GuestPreferences;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Implementación MongoDB del repositorio de GuestPreferences.
 * Gestiona las preferencias de huéspedes en la base de datos NoSQL.
 */
@Repository
public class GuestPreferencesMongoRepository implements GuestPreferencesRepository {

    private final MongoTemplate mongoTemplate;

    public GuestPreferencesMongoRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<GuestPreferences> findByGuestId(String guestId) {
        Query query = new Query(Criteria.where("guestId").is(guestId));
        GuestPreferences preferences = mongoTemplate.findOne(query, GuestPreferences.class);
        return Optional.ofNullable(preferences);
    }

    @Override
    public GuestPreferences save(GuestPreferences preferences) {
        return mongoTemplate.save(preferences);
    }

    @Override
    public void deleteByGuestId(String guestId) {
        Query query = new Query(Criteria.where("guestId").is(guestId));
        mongoTemplate.remove(query, GuestPreferences.class);
    }
}