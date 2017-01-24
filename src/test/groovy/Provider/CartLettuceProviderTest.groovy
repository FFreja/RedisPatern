package Provider

import api.Cart
import com.lambdaworks.redis.RedisClient
import lectture.Provider.CartLettuceProvider
import spock.lang.Specification

class CartLettuceProviderTest extends Specification {

    CartLettuceProvider provider = new CartLettuceProvider()

    void 'should save cart to redis' () {
        given:
        RedisClient client = new RedisClient("127.0.0.1",6379)
        def cart = new Cart(name:'cart', id:123)
        provider.save(cart)

        expect:
        client.connect().async().hmget("cart:123")
    }
}
