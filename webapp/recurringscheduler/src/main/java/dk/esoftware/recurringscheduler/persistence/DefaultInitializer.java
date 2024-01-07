package dk.esoftware.recurringscheduler.persistence;

import io.quarkus.runtime.Startup;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Startup
public class DefaultInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DefaultInitializer.class);

    @Inject
    public DefaultInitializer(
            EntityManager em,
            @ConfigProperty(name = "ADMIN_ACCOUNT_EMAIL", defaultValue = "admin@localhost") String adminEmail,
            @ConfigProperty(name = "ADMIN_ACCOUNT_NAME", defaultValue = "admin") String adminName,
            @ConfigProperty(name = "ADMIN_ACCOUNT_PASSWORD", defaultValue = "superSecretPassword123") String adminPassword
            ) {
        DefaultInitializationUtility.initializeStorageWithDefaults(em, new UserEntity(adminEmail, adminName), adminPassword);
        logger.info("Successfully initialized DB with default values");
    }


}
