package resource

import api.Cart
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang3.StringUtils
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

class CartResourceTest extends Specification{

    CartResource resource = new CartResource()

    void 'should get cart'() {
        given:
        resource.createCart(cartJson())
        byte[] expectResponse = Files.readAllBytes(Paths.get("schema/cart-response.json"))

        expect:
        def response = resource.getCart(123)

        response == new String(expectResponse)
    }

    def cartJson() {
        ObjectMapper mapper = new ObjectMapper()
        Cart cart = mapper.readValue(new File("schema/cart-request.json"), Cart.class)
        cart
    }


}
