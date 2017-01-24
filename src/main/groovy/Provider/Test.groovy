package Provider

import api.Cart
import api.Item
import com.example.tutorial.CartProtos
import redis.clients.jedis.Jedis

class Test {
    static Jedis jedis = new Jedis("127.0.0.1",6379)

    public static void main(String[] args) {
        def cart = buildCartProto()

        def connection = jedis.connect()

        jedis.set("cart".bytes, cart.toByteArray())
        def proto = CartProtos.Cart.parseFrom(jedis.get("cart".bytes))
        System.out.println(proto.toString())

        jedis.close()
    }

    private static CartProtos.Cart buildCartProto() {
        def item1 = new Item(name:'name1', id: "1", brand: "brand1", stockType: 0)
        def item2 = new Item(name:'name2', id: "2", brand: "brand2", stockType: 1)
        def items = [item1, item2]
        def cart = new Cart(name:'cart', id:123)
        def builder = CartProtos.Cart.newBuilder()
        builder.setId(cart.Id)
                .setName(cart.name).build()
        builder.addAllItem(buildItems(items))
        builder.build()
    }

    private static List buildItems(List items) {
        List<CartProtos.Item> itemProtos = new ArrayList<>()
        items.each{
            itemProtos.add(CartProtos.Item.newBuilder().setName(it.name).setId(it.id).
                    setBrand(it.brand).setType(CartProtos.Item.StockTypes.forNumber(it.stockType)).build())
        }
        return itemProtos
    }
}
