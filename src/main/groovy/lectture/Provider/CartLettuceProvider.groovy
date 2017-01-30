package lectture.Provider

import com.google.inject.Inject
import com.lambdaworks.redis.RedisClient
import com.lambdaworks.redis.api.StatefulRedisConnection
import com.lambdaworks.redis.codec.CompressionCodec
import lectture.SerializedObjectCodec

class CartLettuceProvider {

    @Inject
    RedisClient client = new RedisClient("127.0.0.1", 6379)

    void save(lectture.api.Cart cart) {
        StatefulRedisConnection<String, Object> connection = client.connect(
                CompressionCodec.valueCompressor(new SerializedObjectCodec(), CompressionCodec.CompressionType.GZIP)).sync();

        def commands = connection.async()
        connection.close()
    }


}
