package Provider

import api.Cart
import api.Item
import codec.CartCodec
import com.example.tutorial.CartProtos
import com.google.inject.Inject
import com.google.protobuf.util.JsonFormat
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

    //Async POST, PUT, PATCH to improve performance
    void save(Cart cart) {
        def connection = client.connect(new CartCodec())

        def commands = connection.async()
        def future = commands.set("cart:${cart.id}", buildCartProto(cart).toByteArray())

        future.thenAccept(new Consumer<String>() {
            @Override
            void accept(String s) {
                connection.close()
            }
        })
    }

    String get(int id) {
        def connection = client.connect(new CartCodec())
        def commands = connection.sync()
        def cart = commands.get("cart:${id}")

        CartProtos.Cart from = CartProtos.Cart.parseFrom(cart)
        connection.close()
        return JsonFormat.printer().print(from)
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
