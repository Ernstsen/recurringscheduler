package dk.esoftware.recurringscheduler.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public interface DomainEntityManager<T> {

    /**
     * @return list of all existing entities of specified type
     */
    List<T> getEntities();

    /**
     * @param id identifier for the wanted entity
     * @return the entity for the given id, if one exists. Null otherwise
     */
    T getEntity(String id);

    /**
     * Creates an entity with the given data
     *
     * @param entity the entity to persist/create
     */
    void createEntity(T entity);

    /**
     * Deletes an entity
     *
     * @param id identifier for the entity to be deleted
     */
    void deleteEntity(String id);

}
