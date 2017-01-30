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
    RedisClient client

    @Inject
    CartCodec codec
//    RedisClient client = RedisClient.create("redis://localhost")

    /**
     * get cart from cache and convert to json
     * @param id
     * @return
     */
    String get(int id) {
        def connection = client.connect(codec)
        def commands = connection.sync()
        def cart = commands.get("cart:${id}")

        CartProtos.Cart from = CartProtos.Cart.parseFrom(cart)
        connection.close()
        return JsonFormat.printer().print(from)
    }


    //Async POST, PUT, PATCH to update cart cache
    void save(Cart cart) {
        def connection = client.connect(codec)

        def commands = connection.async()
        def future = commands.set("cart:${cart.id}", buildCartProto(cart).toByteArray())

        future.thenAccept(new Consumer<String>() {
            @Override
            void accept(String s) {
                connection.close()
            }
        })
    }

    /**
     * Can directly build message from json string for simple requirement.
     * Need customize for complex requirement
     * @param cart
     * @return
     */
    private CartProtos.Cart buildCartProto(Cart cart) {
        def cartProto = CartProtos.Cart.newBuilder()
        cartProto.setName(cart.name)
        cartProto.setId(cart.id)

        cart.items.each {
            cartProto.addItems(buildItem(it))
        }

        cartProto.build()
    }

    private CartProtos.Item buildItem(Item it) {
        def item = CartProtos.Item.newBuilder()
        item.setName(it.name).setId(it.id).
                setBrand(it.brand).setStockType(CartProtos.Item.StockTypes.forNumber(it.stockType.code))
        item.build()
    }
}
