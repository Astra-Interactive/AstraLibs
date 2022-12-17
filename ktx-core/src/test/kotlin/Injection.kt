import ru.astrainteractive.astralibs.di.IDependency
import ru.astrainteractive.astralibs.di.alsoRemember
import ru.astrainteractive.astralibs.di.getValue
import ru.astrainteractive.astralibs.di.module
import kotlin.test.Test
import kotlin.test.assertEquals


class Injection {
    @Test
    fun InjectionTest() {
        val myModule = module {
            "My string"
        }.alsoRemember()
        val injectedModule: IDependency<String> = IDependency.get()
        val value by injectedModule
        val value2: String by IDependency
        assertEquals(myModule.value, value)
        assertEquals(myModule.value, value2)
        assertEquals(value, value2)

    }
}