import Provider.CartProvider
import com.google.inject.Provides
import io.dropwizard.setup.Environment

class TutorialModule {

    private final TutorialConfig config
    private final Environment environment

    @Provides
    CartProvider prepareProvider(){
        new CartProvider()
    }



}
