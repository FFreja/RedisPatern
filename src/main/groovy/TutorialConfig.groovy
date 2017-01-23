import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration
import redis.clients.jedis.JedisFactory

import javax.validation.constraints.NotNull

class TutorialConfig extends Configuration {
    String cacheHost
    String cachePort

    @JsonProperty("swagger")
    SwaggerBundleConfiguration swaggerBundleConfiguration

    @NotNull
    @JsonProperty
    JedisFactory jedisFactory

}
