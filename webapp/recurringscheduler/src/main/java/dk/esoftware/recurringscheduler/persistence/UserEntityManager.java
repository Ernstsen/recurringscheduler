package dk.esoftware.recurringscheduler.persistence;

import dk.esoftware.recurringscheduler.domain.DomainEntityManager;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

public class UserEntityManager implements DomainEntityManager<UserEntity> {

    private final EntityManager entityManager;
    private final QueryHelper<UserEntity> queryHelper;

    @Inject
    public UserEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        queryHelper = new QueryHelper<>(entityManager, UserEntity.class);
    }

    @Override
    public List<UserEntity> getEntities() {
        return queryHelper.getAll();
    }

    @Override
    public UserEntity getEntity(UUID id) {
        return entityManager.find(UserEntity.class, id);
    }

    @Override
    public void createEntity(UserEntity entity) {
        entityManager.persist(entity);
    }

    @Override
    public void deleteEntity(UUID id) {
        final UserEntity entity = getEntity(id);

        if (entity != null) {
            entityManager.remove(entity);
        }
    }
}
