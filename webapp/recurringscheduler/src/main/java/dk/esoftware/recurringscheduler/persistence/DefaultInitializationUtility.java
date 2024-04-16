package dk.esoftware.recurringscheduler.persistence;

import dk.esoftware.recurringscheduler.domain.AuthenticationManager;
import dk.esoftware.recurringscheduler.domain.TimeUnit;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultInitializationUtility {
    private static final Logger logger = LoggerFactory.getLogger(DefaultInitializationUtility.class);

    private static final Map<String, Supplier<RecurrenceConfiguration>> defaults = getDefaults();

    @Transactional
    public static void initializeStorageWithDefaults(EntityManager entityManager, UserEntity adminUser, String adminPassword) {
        initializeDefaultRecurrenceConfigurations(entityManager);
        createAdminUser(entityManager, adminUser, adminPassword);
    }

    private static void createAdminUser(EntityManager entityManager, UserEntity adminUser, String adminPassword) {
        final UserEntityManager userManager = new UserEntityManager(entityManager);

        final UserEntity existingAdmin = userManager.getUserByEmail(adminUser.getEmail());

        if (existingAdmin != null) {
            logger.info("Admin user already exists in DB - skipping creation, but updating values");
            existingAdmin.setName(adminUser.getName());
        } else {
            logger.info("Did not find admin user in DB - creating it");
            userManager.createEntity(adminUser);
            new AuthenticationManager(entityManager).setPassword(adminUser, adminPassword);
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

    @Transactional
    public static void initializeStorageWithDemoData(EntityManager entityManager) {
        UserEntityManager userManager = new UserEntityManager(entityManager);

        final String demoMail = "john.doe@demo.local";
        final UserEntity user = userManager.getUserByEmail(demoMail);

        if (user != null) {
            logger.info("Demo user already exists in DB - skipping demo data screation");
            return;
        }

        final UserEntity user1 = new UserEntity(demoMail, "John Doe");
        userManager.createEntity(user1);

        final RecurrenceConfiguration onceAWeek = new DefaultEntityManager<>(entityManager, RecurrenceConfiguration.class)
                .getEntities().stream().filter(r -> r.getName().contains("week")).
                findFirst().orElseThrow(() -> new IllegalStateException("Did not find default recurrence configuration 'Once a week' in DB"));

        final DefaultEntityManager<EventType> eventManager = new DefaultEntityManager<>(entityManager, EventType.class);
        final EventType eventType = new EventType("Dungeons and Dragons", onceAWeek);
        eventManager.createEntity(eventType);
        eventType.getParticipatingUsers().add(user1);

        new DefaultEntityManager<>(entityManager, Event.class).createEntity(new Event(
                "Dungeons and Dragons - Session 1",
                eventType,
                user1,
                List.of(
                        LocalDate.of(2024, 3, 2),
                        LocalDate.of(2024, 3, 9),
                        LocalDate.of(2024, 3, 16)
                ),
                null
        ));
    }
}
