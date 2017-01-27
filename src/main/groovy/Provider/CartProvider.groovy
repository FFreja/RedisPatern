package Provider

import api.Cart
import api.Item
import codec.CartCodec
import com.example.tutorial.CartProtos
import com.google.inject.Inject
import com.lambdaworks.redis.RedisClient
import com.lambdaworks.redis.RedisFuture
import com.lambdaworks.redis.api.StatefulRedisConnection
import com.lambdaworks.redis.api.async.RedisAsyncCommands
import com.sun.org.apache.xpath.internal.operations.Bool
import javassist.bytecode.ByteArray
import org.apache.commons.beanutils.BeanUtils

import java.util.function.BiConsumer
import java.util.function.Consumer

class CartProvider {

    @Inject
//    RedisClient client

    RedisClient client = RedisClient.create("redis://localhost")

    void save(Cart cart) {
        def connection = client.connect(new CartCodec())

        def commands = connection.async()
        def future = commands.set("cart:${cart.id}", buildCartProto())
        future.whenComplete(new BiConsumer<String, Throwable>() {
            @Override
            void accept(String s, Throwable throwable) {
                connection.close()
            }
        })
    }

    void get(int id) {

    }

    Cart get(String id) {
        def cart
        def connection = client.connect()

        def commands = connection.async()
        RedisFuture<String> future = commands.hgetall("cart:${id}")
        future.thenAccept(new Consumer<String>() {
            @Override
            void accept(String s)  {

                cart = CartProtos.Cart.parseFrom(s)
                connection.close()
            }
        })

        return cart
    }

    private CartProtos.Cart buildCartProto(Cart cart) {
        def cartProto = CartProtos.Cart.newBuilder()
        cartProto.setName(cart.name)
        cartProto.setId(cart.id)

        cart.items.each {
            cartProto.addItem(buildItem(it))
        }

        cartProto.build()
    }

    private CartProtos.Item buildItem(Item it) {
        def item = CartProtos.Item.newBuilder()
        item.setName(it.name).setId(it.id).
                setBrand(it.brand).setType(CartProtos.Item.StockTypes.forNumber(it.stockType))
        item.build()
    }
}
