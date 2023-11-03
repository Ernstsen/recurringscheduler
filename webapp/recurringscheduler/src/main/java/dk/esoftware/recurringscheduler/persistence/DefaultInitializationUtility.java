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

    private static final Map<String, Supplier<RecurConfig>> defaults = getDefaults();

    public static void InitializeStorageWithDefaults(EntityManager entityManager) {
        for (String name : defaults.keySet()) {

            final List defaultConfig = entityManager.createQuery("select rc from RecurConfig rc where rc.name = :defaultName")
                    .setParameter("defaultName", name)
                    .getResultList();

            if (defaultConfig.isEmpty()) {
                logger.info("Did not find default recurrence configuration '{}' in DB - creating it", name);
                final RecurConfig entity = defaults.get(name).get();
                entityManager.persist(entity);
            }
        }
    }

    private static Map<String, Supplier<RecurConfig>> getDefaults() {
        final HashMap<String, Supplier<RecurConfig>> defaults = new HashMap<>();

        defaults.put("Once a week", () -> new RecurConfig("Once a week", TimeUnit.WEEK, 1));
        defaults.put("Once a month", () -> new RecurConfig("Once a month", TimeUnit.MONTH, 1));
        defaults.put("Twice a year", () -> new RecurConfig("Twice a year", TimeUnit.YEAR, 2));

        return defaults;
    }

}
