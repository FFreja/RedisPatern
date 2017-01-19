import com.google.inject.Guice
import com.google.inject.Injector
import io.dropwizard.Application
import io.dropwizard.configuration.ResourceConfigurationSourceProvider
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.federecio.dropwizard.swagger.SwaggerBundle
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration
import redis.clients.jedis.JedisFactory
import resource.CartResource

class TutorialApplication extends Application<TutorialConfig>{

    @Override
    void run(TutorialConfig configuration, Environment environment) throws Exception {
        Injector injector = Guice.createInjector(new TutorialModule(configuration, environment))
        environment.jersey().register(injector.getInstance(CartResource))

    }

    @Override
    void initialize(Bootstrap<TutorialConfig> bootstrap) {

        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider())
        bootstrap.addBundle(new SwaggerBundle<TutorialConfig>() {
            @Override
            JedisFactory getJedisFactory(TutorialConfig config) {
                return config.getJedisFactory()
            }
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(TutorialConfig config) {
                return config.getSwaggerBundleConfiguration()
            }
        })
    }
}
