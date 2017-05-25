package net.spals.leagur.app;

import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.spals.appbuilder.app.dropwizard.DropwizardWebApp;
import net.spals.appbuilder.config.service.ServiceScan;
import net.spals.appbuilder.executor.core.ExecutorServiceFactory;
import net.spals.appbuilder.mapstore.core.MapStore;
import net.spals.appbuilder.mapstore.core.migration.MapStoreMigrationRunner;
import net.spals.appbuilder.model.core.ModelSerializer;
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

    private DropwizardWebApp.Builder webAppDelegateBuilder;
    private DropwizardWebApp webAppDelegate;

    @Override
    public String getName() {
        return "leagur";
    }

    @Override
    public void initialize(final Bootstrap<Configuration> bootstrap) {
        // Run a service scan on all leagur packages as well as appbuilder service packages
        this.webAppDelegateBuilder = new DropwizardWebApp.Builder(bootstrap, LOGGER)
            .setServiceConfigFromClasspath(SERVICE_CONFIG_FILE_NAME)
            .setServiceScan(new ServiceScan.Builder()
                .addServicePackages("net.spals.leagur")
                .addDefaultServices(ExecutorServiceFactory.class)
                .addDefaultServices(MapStore.class)
                .addDefaultServices(ModelSerializer.class)
                .build());
    }

    @Override
    public void run(final Configuration configuration, final Environment env) throws Exception {
        this.webAppDelegate = webAppDelegateBuilder.setEnvironment(env).build();

        final Injector serviceInjector = webAppDelegate.getServiceInjector();
        // Run all MapStore migrations
        final MapStoreMigrationRunner migrationRunner = serviceInjector.getInstance(MapStoreMigrationRunner.class);
        migrationRunner.runMigrations();

        final ExecutorServiceFactory executorServiceFactory = serviceInjector.getInstance(ExecutorServiceFactory.class);
        executorServiceFactory.createFixedThreadPool(1, getClass());
    }
}
