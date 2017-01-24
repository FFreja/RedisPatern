package Provider

import api.Cart
import com.example.tutorial.CartProtos
import com.google.inject.Inject
import com.lambdaworks.redis.RedisClient
import com.lambdaworks.redis.RedisFuture
import com.lambdaworks.redis.api.StatefulRedisConnection
import com.lambdaworks.redis.api.async.RedisAsyncCommands
import javassist.bytecode.ByteArray

import java.util.function.Consumer

class CartProvider {

    @Inject
//    RedisClient client

    RedisClient client = RedisClient.create("redis://localhost")

    void save(Cart cart) {
        def cartProto = buildCartProto(cart)
        def connection = client.connect()

        def commands = connection.async()
        def future = commands.set("cart:${cart.id}", "123")
        future.thenAccept(new Consumer<Boolean>() {
            @Override
            void accept(Boolean aBoolean) {
//                connection.close()
            }
        })
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
        def builder = CartProtos.Cart.newBuilder()
        builder.setId(cart.Id)
                .setName(cart.name).build()
        builder.addAllItem(buildItems(cart.items))
        builder.build()
    }

    private List buildItems(List items) {
        List<CartProtos.Item> itemProtos = new ArrayList<>()
         items.each{
             itemProtos.add(CartProtos.Item.newBuilder().setName(it.name).setId(it.id).
                     setBrand(it.brand).setType(CartProtos.Item.StockTypes.forNumber(it.stockType)).build())
        }
        return itemProtos
    }
}
