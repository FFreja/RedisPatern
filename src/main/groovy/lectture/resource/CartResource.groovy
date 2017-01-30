package lectture.resource

import com.google.inject.Inject
import lectture.Provider.CartLettuceProvider

import javax.ws.rs.POST
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Produces(MediaType.APPLICATION_JSON)
class CartResource {

    @Inject
    CartLettuceProvider provider

    @POST
    void createCart(lectture.api.Cart cart) {
        provider.save(cart)
    }

}
