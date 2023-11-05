package dk.esoftware.recurringscheduler.persistence;

import dk.esoftware.recurringscheduler.domain.TimeUnit;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultInitializationUtility {
    private static final Logger logger = LoggerFactory.getLogger(DefaultInitializationUtility.class);

    private static final Map<String, Supplier<RecurranceConfiguration>> defaults = getDefaults();

    public static void InitializeStorageWithDefaults(EntityManager entityManager) {
        for (String name : defaults.keySet()) {

            final List defaultConfig = entityManager.createQuery("select rc from RecurranceConfiguration rc where rc.name = :defaultName")
                    .setParameter("defaultName", name)
                    .getResultList();

            if (defaultConfig.isEmpty()) {
                logger.info("Did not find default recurrence configuration '{}' in DB - creating it", name);
                final RecurranceConfiguration entity = defaults.get(name).get();
                entityManager.persist(entity);
            }
        }
    }

    private static Map<String, Supplier<RecurranceConfiguration>> getDefaults() {
        final HashMap<String, Supplier<RecurranceConfiguration>> defaults = new HashMap<>();

        defaults.put("Once a week", () -> new RecurranceConfiguration("Once a week", TimeUnit.WEEK, 1));
        defaults.put("Once a month", () -> new RecurranceConfiguration("Once a month", TimeUnit.MONTH, 1));
        defaults.put("Twice a year", () -> new RecurranceConfiguration("Twice a year", TimeUnit.YEAR, 2));

        return defaults;
    }

}
