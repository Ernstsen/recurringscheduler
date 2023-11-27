package dk.esoftware.recurringscheduler.persistence;

import dk.esoftware.recurringscheduler.domain.DomainEntityManager;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

public class EventEntityManager implements DomainEntityManager<Event> {

    private final EntityManager entityManager;
    private final QueryHelper<Event> queryHelper;

    @Inject
    public EventEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        queryHelper = new QueryHelper<>(entityManager, Event.class);
    }

    @Override
    public List<Event> getEntities() {
        return queryHelper.getAll();
    }

    @Override
    public Event getEntity(UUID id) {
        return entityManager.find(Event.class, id);
    }

    @Override
    public void createEntity(Event entity) {
        entityManager.persist(entity);
    }

    @Override
    public void deleteEntity(UUID id) {
        final Event entity = getEntity(id);

        if (entity != null) {
            entityManager.remove(entity);
        }
    }
}
