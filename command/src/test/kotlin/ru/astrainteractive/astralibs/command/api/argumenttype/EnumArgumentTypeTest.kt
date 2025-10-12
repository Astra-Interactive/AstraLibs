package ru.astrainteractive.astralibs.command.api.argumenttype

import org.junit.Assert.assertThrows
import org.junit.Test
import ru.astrainteractive.astralibs.command.api.exception.ArgumentTypeException
import kotlin.test.assertEquals

class EnumArgumentTypeTest {
    enum class TestType(override val value: String) : EnumArgument {
        QS("qs"), SHOP("shop")
    }

    @Test
    fun `GIVEN_enum_WHEN_parse_THEN_parsed`() {
        TestType.entries.forEach { entry ->
            EnumArgumentType(TestType.entries).transform(entry.value).let { result ->
                assertEquals(entry, result)
            }
        }
        assertThrows(ArgumentTypeException::class.java) {
            EnumArgumentType(TestType.entries).transform("no_value")
        }
    }
}
