package Provider

import api.Cart
import api.Item
import api.StockType
import com.example.tutorial.CartProtos
import spock.lang.Specification

class CartProviderTest extends Specification {
    CartProvider provider = new CartProvider()

    void 'should build an item proto'() {
        given:
        def item = new Item(name: 'name1', id: "1", brand: "brand1", stockType: StockType.DEMAND)

        def itemProto = provider.buildItem(item)

        expect:
        itemProto == CartProtos.Item.newBuilder().setName(item.getName()).setId(item.getId())
                .setBrand(item.getBrand()).setStockType(CartProtos.Item.StockTypes.forNumber(item.getStockType().code)).build()
    }

    void 'should build a cart proto'() {
        given:
        def item1 = new Item(name: 'name1', id: "1", brand: "brand1", stockType: StockType.REMOTE)
        def cart = new Cart(name: 'cart', id: 123)
        cart.items = [item1]
        def cartProto = provider.buildCartProto(cart)

        expect:
        cartProto == CartProtos.Cart.newBuilder().setName(cart.name).setId(cart.id)
                .addItems(CartProtos.Item.newBuilder().setName(item1.getName()).setId(item1.getId())
                .setBrand(item1.getBrand()).setStockType(CartProtos.Item.StockTypes.forNumber(item1.getStockType().code)).build()).build()
    }

    void 'should build cartProto with items'() {
        given:
        def item1 = new Item(name: 'name1', id: "1", brand: "brand1", stockType: StockType.DEMAND)
        def item2 = new Item(name: 'name2', id: "2", brand: "brand2", stockType: StockType.REMOTE)
        def cart = new Cart(name: 'cart', id: 123)
        cart.items = [item1, item2]

        CartProtos.Cart cartProto = provider.buildCartProto(cart)

        expect:
        cartProto.getId() == 123
        cartProto.itemsCount == 2
        cartProto.getItems(0) == CartProtos.Item.newBuilder().setName(item1.getName()).setId(item1.getId())
                .setBrand(item1.getBrand()).setStockType(CartProtos.Item.StockTypes.forNumber(item1.getStockType().code)).build()
    }

}
