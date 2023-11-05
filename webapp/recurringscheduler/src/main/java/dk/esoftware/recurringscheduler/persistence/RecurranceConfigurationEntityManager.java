package dk.esoftware.recurringscheduler.persistence;

import dk.esoftware.recurringscheduler.domain.DomainEntityManager;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

public class RecurranceConfigurationEntityManager implements DomainEntityManager<RecurranceConfiguration> {

    private final EntityManager entityManager;
    private final QueryHelper<RecurranceConfiguration> queryHelper;

    @Inject
    public RecurranceConfigurationEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        queryHelper = new QueryHelper<>(entityManager, RecurranceConfiguration.class);
    }

    @Override
    public List<RecurranceConfiguration> getEntities() {
        return queryHelper.getAll();
    }

    @Override
    public RecurranceConfiguration getEntity(String id) {
        return entityManager.find(RecurranceConfiguration.class, id);
    }

    @Override
    public void createEntity(RecurranceConfiguration entity) {
        entityManager.persist(entity);
    }

    @Override
    public void deleteEntity(String id) {
        entityManager.remove(id);
    }
}
