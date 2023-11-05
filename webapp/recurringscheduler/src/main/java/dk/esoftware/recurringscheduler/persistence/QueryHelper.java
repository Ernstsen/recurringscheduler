package dk.esoftware.recurringscheduler.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

class QueryHelper<T> {

    private final Class<T> type;
    private final EntityManager entityManager;

    public QueryHelper(EntityManager entityManager, Class<T> type) {
        this.entityManager = entityManager;
        this.type = type;
    }

    List<T> getAll() {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> cr = criteriaBuilder.createQuery(type);
        Root<T> root = cr.from(type);
        cr.select(root);

        return entityManager.createQuery(cr).getResultList();
    }

}
