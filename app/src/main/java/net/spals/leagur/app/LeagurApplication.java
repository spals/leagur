package net.spals.leagur.app;

import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.spals.appbuilder.app.dropwizard.DropwizardApp;
import net.spals.appbuilder.app.dropwizard.DropwizardManagedWrapper;
import net.spals.appbuilder.executor.core.ManagedExecutorServiceRegistry;
import net.spals.appbuilder.mapstore.core.migration.MapStoreMigrationRunner;
import net.spals.appbuilder.message.core.consumer.MessageConsumer;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tkral
 */
public final class LeagurApplication extends Application<Configuration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeagurApplication.class);

    private static final String APP_CONFIG_FILE_NAME = "config/leagur-app.yml";
    private static final String SERVICE_CONFIG_FILE_NAME = "config/leagur-service.conf";

    public static void main(final String[] args) throws Throwable {
        new LeagurApplication().run("server", APP_CONFIG_FILE_NAME);
    }

    private DropwizardApp.Builder appBuilder;

    @Override
    public String getName() {
        return "leagur";
    }

    @Override
    public void initialize(final Bootstrap<Configuration> bootstrap) {
        // Run a service scan on all leagur packages as well as appbuilder service packages
        final Reflections serviceScan = new Reflections("net.spals.leagur",
                "net.spals.appbuilder.executor.core",
                "net.spals.appbuilder.mapstore.core",
                "net.spals.appbuilder.message.core");

        this.appBuilder = new DropwizardApp.Builder(bootstrap)
            .setLogger(LOGGER)
            .setServiceConfigFromClasspath(SERVICE_CONFIG_FILE_NAME)
            .setServiceScan(serviceScan);
    }

    @Override
    public void run(final Configuration configuration, final Environment env) throws Exception {
        try {
            final DropwizardApp app = appBuilder.usingEnvironment(env).build();
            final Injector serviceInjector = app.getServiceInjector();

            // Run all MapStore migrations
            final MapStoreMigrationRunner migrationRunner = serviceInjector.getInstance(MapStoreMigrationRunner.class);
            migrationRunner.runMigrations();

            // Attach all ExecutorServices to the Dropwizard lifecycle
            final ManagedExecutorServiceRegistry executorServiceRegistry =
                    serviceInjector.getInstance(ManagedExecutorServiceRegistry.class);
            env.lifecycle().manage(DropwizardManagedWrapper.wrap(executorServiceRegistry));
            // Attach all MessageConsumers to the Dropwizard lifecycle
            final MessageConsumer messageConsumer = serviceInjector.getInstance(MessageConsumer.class);
            env.lifecycle().manage(DropwizardManagedWrapper.wrap(messageConsumer));
        } catch (Throwable t) {
            LOGGER.error("Unexpected error encountered while running " + getName(), t);
            throw t;
        }
    }
}
