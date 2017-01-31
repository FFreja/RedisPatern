import com.google.inject.Inject
import com.lambdaworks.redis.RedisClient
import io.dropwizard.lifecycle.Managed

class RedisClientManager implements Managed {

    @Inject
    RedisClient redisClient

    @Override
    void start() throws Exception {
    }

    @Override
    void stop() throws Exception {
        redisClient.shutdown()
    }
}
