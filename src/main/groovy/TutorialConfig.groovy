import com.fasterxml.jackson.annotation.JsonProperty
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration
import redis.clients.jedis.JedisFactory

import javax.validation.constraints.NotNull

class TutorialConfig {
    String cacheHost
    String cachePort

    @JsonProperty("swagger")
    SwaggerBundleConfiguration swaggerBundleConfiguration

    @NotNull
    @JsonProperty
    JedisFactory jedisFactory

}
