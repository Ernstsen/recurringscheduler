package dk.esoftware.recurringscheduler.persistence;

import dk.esoftware.recurringscheduler.domain.DomainEntityManager;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

public class DefaultEntityManager<Entity> implements DomainEntityManager<Entity>  {
    private final QueryHelper<Entity> queryHelper;
    private final EntityManager entityManager;
    private final Class<Entity> entityClass;

    public DefaultEntityManager(EntityManager entityManager, Class<Entity> entityClass) {
        queryHelper = new QueryHelper<>(entityManager, entityClass);
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }

    @Override
    public List<Entity> getEntities() {
        return queryHelper.getAll();
    }

    @Override
    public Entity getEntity(UUID id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public void createEntity(Entity entity) {
        entityManager.persist(entity);
    }

    @Override
    public void deleteEntity(UUID id) {
        final Entity entity = getEntity(id);

        if (entity != null) {
            entityManager.remove(entity);
        }
    }
}
