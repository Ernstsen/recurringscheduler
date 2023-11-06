package dk.esoftware.recurringscheduler.persistence;

import dk.esoftware.recurringscheduler.domain.DomainEntityManager;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

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
    public RecurrenceConfiguration getEntity(String id) {
        return entityManager.find(RecurrenceConfiguration.class, id);
    }

    @Override
    public void createEntity(RecurrenceConfiguration entity) {
        entityManager.persist(entity);
    }

    @Override
    public void deleteEntity(String id) {
        entityManager.remove(id);
    }
}
