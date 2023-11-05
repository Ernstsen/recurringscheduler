package dk.esoftware.recurringscheduler.persistence;

import dk.esoftware.recurringscheduler.domain.DomainEntityManager;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

public class EventTypeEntityManager implements DomainEntityManager<EventType> {

    private final EntityManager entityManager;
    private final QueryHelper<EventType> queryHelper;

    @Inject
    public EventTypeEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        queryHelper = new QueryHelper<>(entityManager, EventType.class);
    }

    @Override
    public List<EventType> getEntities() {
        return queryHelper.getAll();
    }

    @Override
    public EventType getEntity(String id) {
        return entityManager.find(EventType.class, id);
    }

    @Override
    public void createEntity(EventType entity) {
        entityManager.persist(entity);
    }

    @Override
    public void deleteEntity(String id) {
        entityManager.remove(id);
    }
}
