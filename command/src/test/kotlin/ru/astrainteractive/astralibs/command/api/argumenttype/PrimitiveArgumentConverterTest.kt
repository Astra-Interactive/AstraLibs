package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.command.api.exception.ArgumentConverterException
import ru.astrainteractive.astralibs.command.api.exception.CommandException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class PrimitiveArgumentConverterTest {

    @Test
    fun GIVEN_integer_strings_WHEN_int_converter_THEN_parsed() {
        assertEquals(42, IntArgumentConverter.transform("42"))
        assertEquals(-7, IntArgumentConverter.transform("-7"))
        assertEquals(0, IntArgumentConverter.transform("0"))
    }

    @Test
    fun GIVEN_non_integer_strings_WHEN_int_converter_THEN_throws() {
        assertFailsWith<ArgumentConverterException> { IntArgumentConverter.transform("abc") }
        assertFailsWith<ArgumentConverterException> { IntArgumentConverter.transform("4.5") }
        assertFailsWith<ArgumentConverterException> { IntArgumentConverter.transform("") }
    }

    @Test
    fun GIVEN_any_string_WHEN_string_converter_THEN_returned_as_is() {
        assertEquals("hello world", StringArgumentConverter.transform("hello world"))
        assertEquals("", StringArgumentConverter.transform(""))
    }

    @Test
    fun GIVEN_decimal_strings_WHEN_double_converter_THEN_parsed() {
        assertEquals(3.14, DoubleArgumentConverter.transform("3.14"))
        assertEquals(1000.0, DoubleArgumentConverter.transform("1e3"))
    }

    @Test
    fun GIVEN_non_decimal_string_WHEN_double_converter_THEN_throws() {
        assertFailsWith<ArgumentConverterException> { DoubleArgumentConverter.transform("abc") }
    }

    @Test
    fun GIVEN_decimal_strings_WHEN_float_converter_THEN_parsed() {
        assertEquals(2.5f, FloatArgumentConverter.transform("2.5"))
    }

    @Test
    fun GIVEN_non_decimal_string_WHEN_float_converter_THEN_throws() {
        assertFailsWith<ArgumentConverterException> { FloatArgumentConverter.transform("not-a-float") }
    }

    @Test
    fun GIVEN_strict_boolean_literals_WHEN_boolean_converter_THEN_parsed() {
        assertEquals(true, BooleanArgumentConverter.transform("true"))
        assertEquals(false, BooleanArgumentConverter.transform("false"))
    }

    @Test
    fun GIVEN_non_strict_boolean_strings_WHEN_boolean_converter_THEN_throws() {
        assertFailsWith<ArgumentConverterException> { BooleanArgumentConverter.transform("TRUE") }
        assertFailsWith<ArgumentConverterException> { BooleanArgumentConverter.transform("1") }
        assertFailsWith<ArgumentConverterException> { BooleanArgumentConverter.transform("yes") }
        assertFailsWith<ArgumentConverterException> { BooleanArgumentConverter.transform("") }
    }

    @Test
    fun GIVEN_converter_failure_WHEN_caught_as_command_exception_THEN_succeeds() {
        assertFailsWith<CommandException> { IntArgumentConverter.transform("nope") }
    }

    @Test
    fun GIVEN_string_converter_WHEN_transform_THEN_returns_same_reference() {
        val input = "reference"

        assertSame(input, StringArgumentConverter.transform(input))
    }

    @Test
    fun GIVEN_signed_and_padded_integers_WHEN_int_converter_THEN_parsed() {
        assertEquals(42, IntArgumentConverter.transform("+42"))
        assertEquals(7, IntArgumentConverter.transform("007"))
        assertEquals(Int.MAX_VALUE, IntArgumentConverter.transform("2147483647"))
        assertEquals(Int.MIN_VALUE, IntArgumentConverter.transform("-2147483648"))
    }

    @Test
    fun GIVEN_whitespace_overflow_or_radix_prefix_WHEN_int_converter_THEN_throws() {
        assertFailsWith<ArgumentConverterException> { IntArgumentConverter.transform(" 42 ") }
        assertFailsWith<ArgumentConverterException> { IntArgumentConverter.transform("2147483648") }
        assertFailsWith<ArgumentConverterException> { IntArgumentConverter.transform("0x1F") }
    }

    @Test
    fun GIVEN_special_float_literals_WHEN_double_converter_THEN_parsed() {
        assertTrue(DoubleArgumentConverter.transform("NaN").isNaN())
        assertEquals(Double.POSITIVE_INFINITY, DoubleArgumentConverter.transform("Infinity"))
        assertEquals(0.5, DoubleArgumentConverter.transform(".5"))
    }

    @Test
    fun GIVEN_special_float_literals_WHEN_float_converter_THEN_parsed() {
        assertTrue(FloatArgumentConverter.transform("NaN").isNaN())
        assertEquals(Float.POSITIVE_INFINITY, FloatArgumentConverter.transform("Infinity"))
    }

    @Test
    fun GIVEN_wrong_case_or_padded_boolean_WHEN_boolean_converter_THEN_throws() {
        assertFailsWith<ArgumentConverterException> { BooleanArgumentConverter.transform("True") }
        assertFailsWith<ArgumentConverterException> { BooleanArgumentConverter.transform("False") }
        assertFailsWith<ArgumentConverterException> { BooleanArgumentConverter.transform(" true") }
    }

    @Test
    fun GIVEN_whitespace_and_symbols_WHEN_string_converter_THEN_preserved_verbatim() {
        assertEquals("  spaced  ", StringArgumentConverter.transform("  spaced  "))
        assertEquals("a:b/c", StringArgumentConverter.transform("a:b/c"))
    }
}
