import com.codahale.metrics.health.HealthCheck
import com.google.inject.Inject
import com.lambdaworks.redis.RedisClient
import com.codahale.metrics.health.HealthCheck.Result

class RedisHealthCheck extends HealthCheck{

    @Inject
    RedisClient client

    @Override
    protected Result check() throws Exception {
        def connection = client.connect()
        def pong = connection.sync().ping()

        if ("PONG" == pong) {
            def clientList = connection.sync().clientList()
            connection.close()
            return Result.healthy("Client List : ${clientList}")
        } else {
            connection.close()
            return Result.unhealthy("Could not ping redis")
        }
    }
}
