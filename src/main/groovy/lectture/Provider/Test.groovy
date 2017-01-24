package lectture.Provider

import com.lambdaworks.redis.RedisClient
import com.lambdaworks.redis.api.sync.RedisCommands
import com.lambdaworks.redis.codec.CompressionCodec
import lectture.SerializedObjectCodec
import lectture.api.Cart
import lectture.api.Item

class Test {

    static RedisClient client = new RedisClient("127.0.0.1",6379)

    public static void main(String[] args) {
        def cart = buildCart()

        RedisCommands<String, Object> connection = client
                .connect(CompressionCodec
                .valueCompressor(new SerializedObjectCodec(), CompressionCodec.CompressionType.GZIP))
                .sync()
//        connection.set("lettuce",cart)
//        List<String> list = ["one", "two"]
//        connection.set("list", list)
        connection.set("cart", cart)
        connection.close()

    }

    static testSize(){
        RedisCommands<String, Object> connection = client
                .connect(
                CompressionCodec
                        .valueCompressor(new SerializedObjectCodec(), CompressionCodec.CompressionType.DEFLATE))
                .sync()


        connection.close();
    }


    private static Cart buildCart() {
        def item1 = new Item(name:'name1', id: "1", brand: "brand1", stockType: 0)
        def item2 = new Item(name:'name2', id: "2", brand: "brand2", stockType: 1)
        def items = [item1, item2]
        def cart = new Cart(name:'cart', id:123, items:items)
        return cart
    }
}
