package resource

import Provider.CartProvider
import api.Cart
import com.google.inject.Inject
import redis.clients.jedis.Jedis

import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType


@Produces(MediaType.APPLICATION_JSON)
class CartResource {

    @Inject
    CartProvider provider

    @POST
    void createCart(Cart cart, @Context Jedis jedis){


    }

}
