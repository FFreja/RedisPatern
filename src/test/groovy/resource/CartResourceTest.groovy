package resource

import Provider.CartProvider
import api.Cart
import codec.CartCodec
import com.fasterxml.jackson.databind.ObjectMapper
import com.lambdaworks.redis.RedisClient
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

class CartResourceTest extends Specification {

    CartProvider provider = new CartProvider(client: RedisClient.create("redis://localhost"), codec: new CartCodec())

    CartResource resource = new CartResource(provider: provider)

    void 'should get cart'() {
        given:
        resource.createCart(cartJson())
        byte[] expectResponse = Files.readAllBytes(Paths.get("schema/cart-response.json"))

        expect:
        def response = resource.getCart(123)

        response == new String(expectResponse)
    }

    def cartJson() {
        ObjectMapper mapper = new ObjectMapper()
        Cart cart = mapper.readValue(new File("schema/cart-request.json"), Cart.class)
        cart
    }


}
