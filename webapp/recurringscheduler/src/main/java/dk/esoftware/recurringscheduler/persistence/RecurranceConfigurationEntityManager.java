package dk.esoftware.recurringscheduler.persistence;

import dk.esoftware.recurringscheduler.domain.DomainEntityManager;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

public class RecurranceConfigurationEntityManager implements DomainEntityManager<RecurrenceConfiguration> {

    private final EntityManager entityManager;
    private final QueryHelper<RecurrenceConfiguration> queryHelper;

    @Inject
    public RecurranceConfigurationEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        queryHelper = new QueryHelper<>(entityManager, RecurrenceConfiguration.class);
    }

    @Override
    public List<RecurrenceConfiguration> getEntities() {
        return queryHelper.getAll();
    }

    @Override
    public RecurrenceConfiguration getEntity(UUID id) {
        return entityManager.find(RecurrenceConfiguration.class, id);
    }

    @Override
    public void createEntity(RecurrenceConfiguration entity) {
        entityManager.persist(entity);
    }

    @Override
    public void deleteEntity(UUID id) {
        entityManager.remove(id);
    }
}
