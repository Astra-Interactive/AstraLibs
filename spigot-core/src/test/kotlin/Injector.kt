import com.astrainteractive.astralibs.di.Injector
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class InjectorTest {
    @Test
    fun simpleString() {
        val obj = "Sample object"
        assert(Injector.inject<String?>() == null)
        Injector.remember(obj)
        val injected = Injector.inject<String>()
        assertEquals(obj, injected)
        assert(Injector.inject<Int?>() == null)
    }

    @Test
    fun customClass(){
        val uuid = UUID.randomUUID()
        assert(Injector.inject<UUID?>() == null)
        Injector.remember(uuid)
        val injected = Injector.inject<UUID>()
        assertEquals(uuid, injected)
    }
}