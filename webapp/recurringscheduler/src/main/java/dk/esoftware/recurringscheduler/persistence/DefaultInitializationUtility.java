package dk.esoftware.recurringscheduler.persistence;

import dk.esoftware.recurringscheduler.domain.TimeUnit;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultInitializationUtility {
    private static final Logger logger = LoggerFactory.getLogger(DefaultInitializationUtility.class);

    private static final Map<String, Supplier<RecurrenceConfiguration>> defaults = getDefaults();

    @Transactional
    public static void InitializeStorageWithDefaults(EntityManager entityManager, UserEntity adminUser) {
        initializeDefaultRecurrenceConfigurations(entityManager);
        createAdminUser(entityManager, adminUser);
    }

    private static void createAdminUser(EntityManager entityManager, UserEntity adminUser) {
        final UserEntityManager userManager = new UserEntityManager(entityManager);

        final UserEntity existingAdmin = userManager.getUserByEmail(adminUser.getEmail());

        if (existingAdmin != null) {
            logger.info("Admin user already exists in DB - skipping creation, but updating values");
            existingAdmin.setName(adminUser.getName());
        } else {
            logger.info("Did not find admin user in DB - creating it");
            userManager.createEntity(adminUser);
        }
    }

    private static void initializeDefaultRecurrenceConfigurations(EntityManager entityManager) {
        for (String name : defaults.keySet()) {

            final List defaultConfig = entityManager.createQuery("select rc from RecurrenceConfiguration rc where rc.name = :defaultName")
                    .setParameter("defaultName", name)
                    .getResultList();

            if (defaultConfig.isEmpty()) {
                logger.info("Did not find default recurrence configuration '{}' in DB - creating it", name);
                final RecurrenceConfiguration entity = defaults.get(name).get();
                entityManager.persist(entity);
            }
        }
    }

    private static Map<String, Supplier<RecurrenceConfiguration>> getDefaults() {
        final HashMap<String, Supplier<RecurrenceConfiguration>> defaults = new HashMap<>();

        defaults.put("Once a week", () -> new RecurrenceConfiguration("Once a week", TimeUnit.WEEK, 1));
        defaults.put("Once a month", () -> new RecurrenceConfiguration("Once a month", TimeUnit.MONTH, 1));
        defaults.put("Twice a year", () -> new RecurrenceConfiguration("Twice a year", TimeUnit.YEAR, 2));

        return defaults;
    }

}
