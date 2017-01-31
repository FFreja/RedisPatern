package resource

import Provider.CartProvider
import api.Cart
import com.google.inject.Inject
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Api('/cart service')
@Path('/v1/cart')
@Produces(MediaType.APPLICATION_JSON)
class CartResource {

    @Inject
    CartProvider provider

    @POST
    @ApiOperation('Create cart cache')
    def createCart(Cart cart) {
        provider.save(cart)
    }

    @GET
    @Path('/{id}')
    @ApiOperation('Get cart info from cache by id')
    def getCart(@PathParam('id') int id) {
        provider.get(id)
    }

}
