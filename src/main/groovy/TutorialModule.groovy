import Provider.CartProvider
import com.google.inject.Provides
import com.lambdaworks.redis.RedisClient
import com.lambdaworks.redis.RedisURI
import io.dropwizard.setup.Environment
import lectture.Provider.CartLettuceProvider

class TutorialModule {

    private final TutorialConfig config
    private final Environment environment

    /**
     * Best Practices From Guice: Keep constructors as hidden as possible
     * So inject redis client instead of public the constructor
     * @return
     */
    @Provides
    CartProvider prepareProvider(){
        new CartProvider()
    }

    @Provides
    CartLettuceProvider prepareLettuce(){
        new CartLettuceProvider()
    }

    @Provides
    RedisClient prepareRedisClient() {
        new RedisClient(RedisURI.create(config.cacheHost,config.cachePort))
    }

}
