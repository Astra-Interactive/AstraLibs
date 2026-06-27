package ru.astrainteractive.astralibs.string

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class StringDescExtTest {

    @Test
    fun GIVEN_raw_desc_WHEN_map_THEN_subtype_preserved_and_value_transformed() {
        val result = StringDesc.Raw("hello").map { value -> value.uppercase() }

        assertTrue(result is StringDesc.Raw)
        assertEquals("HELLO", result.raw)
    }

    @Test
    fun GIVEN_plain_desc_WHEN_map_THEN_subtype_preserved_and_value_transformed() {
        val result = StringDesc.Plain("hello").map { value -> value.uppercase() }

        assertTrue(result is StringDesc.Plain)
        assertEquals("HELLO", result.raw)
    }

    @Test
    fun GIVEN_case_sensitive_replace_WHEN_case_differs_THEN_nothing_replaced() {
        val result = StringDesc.Raw("Hello World").replace(oldValue = "hello", newValue = "bye")

        assertEquals("Hello World", result.raw)
    }

    @Test
    fun GIVEN_ignore_case_replace_WHEN_case_differs_THEN_replaced() {
        val result = StringDesc.Plain("Hello World").replace(
            oldValue = "hello",
            newValue = "bye",
            ignoreCase = true
        )

        assertTrue(result is StringDesc.Plain)
        assertEquals("bye World", result.raw)
    }

    @Test
    fun GIVEN_raw_desc_WHEN_plus_string_THEN_raw_appended_and_subtype_preserved() {
        val result = StringDesc.Raw("a") + "b"

        assertEquals(StringDesc.Raw("ab"), result)
    }

    @Test
    fun GIVEN_plain_desc_WHEN_plus_string_THEN_plain_appended_and_subtype_preserved() {
        val result = StringDesc.Plain("a") + "b"

        assertEquals(StringDesc.Plain("ab"), result)
    }

    @Test
    fun GIVEN_raw_plus_raw_WHEN_concatenated_THEN_result_is_raw() {
        val result: StringDesc = StringDesc.Raw("a") + StringDesc.Raw("b")

        assertEquals(StringDesc.Raw("ab"), result)
    }

    @Test
    fun GIVEN_raw_plus_plain_WHEN_concatenated_THEN_result_is_raw() {
        val result: StringDesc = StringDesc.Raw("a") + StringDesc.Plain("b")

        assertEquals(StringDesc.Raw("ab"), result)
    }

    @Test
    fun GIVEN_plain_plus_raw_WHEN_concatenated_THEN_result_is_raw() {
        val result: StringDesc = StringDesc.Plain("a") + StringDesc.Raw("b")

        assertEquals(StringDesc.Raw("ab"), result)
    }

    @Test
    fun GIVEN_plain_plus_plain_WHEN_concatenated_THEN_result_is_plain() {
        val result: StringDesc = StringDesc.Plain("a") + StringDesc.Plain("b")

        assertEquals(StringDesc.Plain("ab"), result)
    }

    @Test
    fun GIVEN_null_desc_WHEN_or_empty_THEN_returns_empty_plain() {
        val result = (null as StringDesc?).orEmpty()

        assertEquals(StringDesc.Plain(""), result)
    }

    @Test
    fun GIVEN_non_null_desc_WHEN_or_empty_THEN_returns_same_value() {
        val original: StringDesc = StringDesc.Raw("kept")

        assertEquals(original, original.orEmpty())
    }

    @Test
    fun GIVEN_null_desc_WHEN_or_THEN_returns_block_result() {
        val fallback: StringDesc = StringDesc.Raw("fallback")

        val result = (null as StringDesc?).or { fallback }

        assertEquals(fallback, result)
    }

    @Test
    fun GIVEN_non_null_desc_WHEN_or_THEN_block_not_invoked() {
        val original: StringDesc = StringDesc.Plain("kept")

        val result = original.or { error("block must not be invoked") }

        assertEquals(original, result)
    }

    @Test
    fun GIVEN_plain_desc_WHEN_to_raw_THEN_converted_to_raw_preserving_value() {
        val result = StringDesc.Plain("text").toRaw()

        assertEquals(StringDesc.Raw("text"), result)
    }

    @Test
    fun GIVEN_raw_desc_WHEN_to_raw_THEN_value_unchanged() {
        val result = StringDesc.Raw("text").toRaw()

        assertEquals(StringDesc.Raw("text"), result)
    }

    @Test
    fun GIVEN_oldvalue_absent_WHEN_replace_THEN_unchanged_and_subtype_preserved() {
        val result = StringDesc.Raw("hello").replace(oldValue = "xyz", newValue = "!")

        assertEquals(StringDesc.Raw("hello"), result)
    }

    @Test
    fun GIVEN_multiple_occurrences_WHEN_replace_THEN_all_replaced() {
        val result = StringDesc.Plain("a.a.a").replace(oldValue = "a", newValue = "b")

        assertEquals(StringDesc.Plain("b.b.b"), result)
    }

    @Test
    fun GIVEN_mixed_case_WHEN_replace_ignore_case_THEN_all_variants_replaced() {
        val result = StringDesc.Raw("aAaA").replace(oldValue = "a", newValue = "x", ignoreCase = true)

        assertEquals(StringDesc.Raw("xxxx"), result)
    }

    @Test
    fun GIVEN_newvalue_contains_oldvalue_WHEN_replace_THEN_single_pass_no_recursion() {
        val result = StringDesc.Plain("cat").replace(oldValue = "cat", newValue = "cats")

        assertEquals(StringDesc.Plain("cats"), result)
    }

    @Test
    fun GIVEN_empty_oldvalue_WHEN_replace_THEN_inserted_around_each_character() {
        val result = StringDesc.Plain("ab").replace(oldValue = "", newValue = "-")

        assertEquals(StringDesc.Plain("-a-b-"), result)
    }

    @Test
    fun GIVEN_block_returns_empty_WHEN_map_THEN_empty_raw_with_subtype_preserved() {
        val result = StringDesc.Raw("hello").map { _ -> "" }

        assertEquals(StringDesc.Raw(""), result)
    }

    @Test
    fun GIVEN_empty_raw_WHEN_plus_string_THEN_appends_to_empty() {
        val result = StringDesc.Plain("") + "x"

        assertEquals(StringDesc.Plain("x"), result)
    }

    @Test
    fun GIVEN_append_empty_string_WHEN_plus_THEN_value_unchanged() {
        val result = StringDesc.Raw("keep") + ""

        assertEquals(StringDesc.Raw("keep"), result)
    }

    @Test
    fun GIVEN_two_empty_descs_WHEN_plus_THEN_empty_with_raw_dominance() {
        val rawResult: StringDesc = StringDesc.Raw("") + StringDesc.Plain("")
        val plainResult: StringDesc = StringDesc.Plain("") + StringDesc.Plain("")

        assertEquals(StringDesc.Raw(""), rawResult)
        assertEquals(StringDesc.Plain(""), plainResult)
    }

    @Test
    fun GIVEN_non_null_raw_WHEN_or_empty_THEN_raw_subtype_preserved() {
        val original: StringDesc = StringDesc.Raw("x")

        val result = original.orEmpty()

        assertTrue(result is StringDesc.Raw)
        assertEquals("x", result.raw)
    }

    @Test
    fun GIVEN_empty_plain_WHEN_to_raw_THEN_empty_raw() {
        assertEquals(StringDesc.Raw(""), StringDesc.Plain("").toRaw())
    }
}
