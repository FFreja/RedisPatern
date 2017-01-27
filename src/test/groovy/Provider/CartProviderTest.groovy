package Provider

import api.Cart
import api.Item
import com.example.tutorial.CartProtos
import com.lambdaworks.redis.RedisClient
import com.lambdaworks.redis.RedisFuture
import spock.lang.Specification

class CartProviderTest extends Specification {
    CartProvider provider = new CartProvider()

    void 'should save cart to redis' () {
        given:
        RedisClient client = new RedisClient("127.0.0.1",6379)
        def cart = new Cart(name:'cart', id:1)
        def future = provider.save(cart)

//        expect:
//        future.isDone() == true
    }

    void 'should get cart from redis' () {
        given:
        RedisClient client = RedisClient.create("redis://localhost")
        def cart = provider.get(1)

        expect:
        cart == ""
    }

    void 'should build an item proto' () {
        given:
        def item = new Item(name:'name1', id: "1", brand: "brand1", stockType: 0)

        def itemProto = provider.buildItem(item)

        expect:
        itemProto == CartProtos.Item.newBuilder().setName(item.getName()).setId(item.getId())
                .setBrand(item.getBrand()).setType(CartProtos.Item.StockTypes.forNumber(item.getStockType())).build()
    }

    void 'should build a cart proto' () {
        given:
        def item1 = new Item(name:'name1', id: "1", brand: "brand1", stockType: 0)
        def cart = new Cart(name:'cart', id:123)
        cart.items = [item1]
        def cartProto = provider.buildCartProto(cart)

        expect:
        cartProto ==  CartProtos.Cart.newBuilder().setName(cart.name).setId(cart.id)
        .addItem(CartProtos.Item.newBuilder().setName(item1.getName()).setId(item1.getId())
                .setBrand(item1.getBrand()).setType(CartProtos.Item.StockTypes.forNumber(item1.getStockType())).build()).build()
    }

    void 'should build cartProto with items' () {
        given:
        def item1 = new Item(name:'name1', id: "1", brand: "brand1", stockType: 0)
        def item2 = new Item(name:'name2', id: "2", brand: "brand2", stockType: 1)
        def cart = new Cart(name:'cart', id:123)
        cart.items = [item1, item2]

        CartProtos.Cart cartProto = provider.buildCartProto(cart)

        expect:
        cartProto.getId() == 123
        cartProto.itemCount == 2
        cartProto.getItem(0) == CartProtos.Item.newBuilder().setName(item1.getName()).setId(item1.getId())
                .setBrand(item1.getBrand()).setType(CartProtos.Item.StockTypes.forNumber(item1.getStockType())).build()
    }

}
