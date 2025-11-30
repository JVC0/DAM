package org.docencia.hotel.persistence.jpa;

import java.util.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public abstract class AbstractJpaRepository<T, ID> {

    @PersistenceContext
    protected EntityManager em;

    private final Class<T> entityClass;

    protected AbstractJpaRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Busca una entidad por su ID.
     * 
     * @param id El identificador de la entidad
     * @return Optional que contiene la entidad si existe, o vacío si no
     */
    public Optional<T> findById(ID id) {
        Optional<T> entity = Optional.ofNullable(em.find(entityClass, id));
        return entity;
    }

    public List<T> findAll() {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        return em.createQuery(jpql, entityClass).getResultList();

    }

    public T save(T entity) { 
        if (em.contains(entity)) {
            return em.merge(entity);
        } else {
            em.persist(entity);
            return entity;
        }
    }
   public boolean deleteById(ID id) { 
        T entity = em.find(entityClass, id);
        if (entity != null) {
            em.remove(entity);
            return true;
        }
        return false;
    }

    public boolean existsById(ID id) { 
    return em.find(entityClass, id) != null;
    }

}
