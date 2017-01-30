import Provider.CartProvider
import codec.CartCodec
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.name.Named
import com.lambdaworks.redis.RedisClient
import com.lambdaworks.redis.RedisURI
import io.dropwizard.setup.Environment
import lectture.Provider.CartLettuceProvider

class TutorialModule extends AbstractModule{

    private final TutorialConfig config
    private final Environment environment


    TutorialModule(final TutorialConfig config, final Environment environment) {
        this.config = config
        this.environment = environment
    }
    /**
     * Best Practices From Guice: Keep constructors as hidden as possible
     * @return
     */
    @Provides
    CartProvider prepareProvider(RedisClient client, CartCodec codec){
        new CartProvider(client: client, codec:codec)
    }

    @Provides
    CartLettuceProvider prepareLettuce(){
        new CartLettuceProvider()
    }

    @Provides
    RedisClient prepareRedisClient() {
        RedisClient.create(RedisURI.create(config.cacheHost,config.cachePort))
    }

    @Provides @Named('codec')
    CartCodec prepareCodec(){
        new CartCodec()
    }

    @Override
    protected void configure() {
        bind(TutorialConfig).toInstance(config)
    }
}
