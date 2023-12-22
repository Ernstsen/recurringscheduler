package dk.esoftware.recurringscheduler.persistence;

import io.quarkus.runtime.Startup;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Startup
public class DefaultInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DefaultInitializer.class);

    @Inject
    public DefaultInitializer(EntityManager em) {
        DefaultInitializationUtility.InitializeStorageWithDefaults(em);
        logger.info("Successfully initialized DB with default values");
    }


}
