package dk.esoftware.recurringscheduler.rest;

import dk.esoftware.recurringscheduler.persistence.DefaultInitializationUtility;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/init")
public class InitializationResource {

    @Inject
    EntityManager entityManager;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String getOverview() {
        DefaultInitializationUtility.InitializeStorageWithDefaults(entityManager);

        return "Did initialization. This should be a POST request though";
    }

}
