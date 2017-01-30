import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration

class TutorialConfig extends Configuration {
    String cacheHost
    int cachePort

    @JsonProperty("swagger")
    SwaggerBundleConfiguration swaggerBundleConfiguration

}
