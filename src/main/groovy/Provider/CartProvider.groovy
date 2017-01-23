package Provider

import api.Cart
import com.example.tutorial.CartProtos

class CartProvider {

    void saveCart(Cart cart) {
        def cartProto = buildCartProto(cart)

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
