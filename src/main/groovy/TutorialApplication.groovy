import com.codahale.metrics.health.HealthCheck
import com.google.inject.Guice
import com.google.inject.Injector
import io.dropwizard.Application
import io.dropwizard.configuration.ResourceConfigurationSourceProvider
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.federecio.dropwizard.swagger.SwaggerBundle
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration
import resource.CartResource

class TutorialApplication extends Application<TutorialConfig> {

    static void main(String[] args) throws Exception {
        new TutorialApplication().run(args)
    }

    @Override
    void run(TutorialConfig configuration, Environment environment) throws Exception {
        Injector injector = Guice.createInjector(new TutorialModule(configuration, environment))
        environment.jersey().register(injector.getInstance(CartResource))
        environment.healthChecks().register("redis", injector.getInstance(RedisHealthCheck))
        environment.lifecycle().manage(injector.getInstance(RedisClientManager))
    }

    @Override
    void initialize(Bootstrap<TutorialConfig> bootstrap) {

        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider())

//        bootstrap.addBundle(new JedisBundle<TutorialConfig>() {
//            @Override
//            JedisFactory getJedisFactory(TutorialConfig configuration) {
//                return configuration.getJedisFactory()
//            }
//
//        })

        bootstrap.addBundle(new SwaggerBundle<TutorialConfig>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(TutorialConfig config) {
                return config.getSwaggerBundleConfiguration()
            }
        })
    }
}
