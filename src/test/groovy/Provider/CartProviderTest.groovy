package Provider

import api.Cart
import api.Item
import com.example.tutorial.CartProtos
import spock.lang.Specification

class CartProviderTest extends Specification {
    CartProvider provider = new CartProvider()

    void 'should add items' () {
        given:
        def item1 = new Item(name:'name1', id: "1", brand: "brand1", stockType: 0)
        def item2 = new Item(name:'name2', id: "2", brand: "brand2", stockType: 1)
        def items = [item1, item2]

        def resultItems = provider.buildItems(items)

        expect:
        resultItems.size() == 2
        resultItems.get(0) == CartProtos.Item.newBuilder().setName(item1.getName()).setId(item1.getId())
                .setBrand(item1.getBrand()).setType(CartProtos.Item.StockTypes.forNumber(item1.getStockType())).build()
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
}