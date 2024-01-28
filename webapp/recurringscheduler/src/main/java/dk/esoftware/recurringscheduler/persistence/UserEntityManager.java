package dk.esoftware.recurringscheduler.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class UserEntityManager extends DefaultEntityManager<UserEntity> {

    private final EntityManager entityManager;

    public UserEntityManager(EntityManager entityManager) {
        super(entityManager, UserEntity.class);
        this.entityManager = entityManager;
    }

    public UserEntity getUserByEmail(String email) {
        try {
            return entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.email = :email", UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
