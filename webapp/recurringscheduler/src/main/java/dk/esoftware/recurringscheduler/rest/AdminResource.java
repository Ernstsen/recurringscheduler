package dk.esoftware.recurringscheduler.rest;

import dk.esoftware.recurringscheduler.domain.Overview;
import dk.esoftware.recurringscheduler.persistence.DefaultInitializationUtility;
import dk.esoftware.recurringscheduler.persistence.EventType;
import dk.esoftware.recurringscheduler.persistence.RecurrenceConfiguration;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/admin")
public class AdminResource {

    @Inject
    EntityManager entityManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/overview")
    public Overview getOverview() {
        return new Overview(
                getAll(EventType.class),
                getAll(RecurrenceConfiguration.class)
        );
    }

    private <T> List<T> getAll(Class<T> type) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> cr = criteriaBuilder.createQuery(type);
        Root<T> root = cr.from(type);
        cr.select(root);

        return entityManager.createQuery(cr).getResultList();
    }




}
