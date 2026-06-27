package ru.astrainteractive.astralibs.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

@Suppress("TestFunctionName")
class StringListExtTest {

    private val names = listOf("Steve", "Alex", "steve_alt", "Notch")

    @Test
    fun GIVEN_null_entry_WHEN_with_entry_THEN_returns_same_instance() {
        assertSame(names, names.withEntry(null))
    }

    @Test
    fun GIVEN_empty_entry_WHEN_with_entry_THEN_returns_same_instance() {
        assertSame(names, names.withEntry(""))
    }

    @Test
    fun GIVEN_entry_WHEN_with_entry_ignore_case_THEN_filters_case_insensitively() {
        val result = names.withEntry("steve")

        assertEquals(listOf("Steve", "steve_alt"), result)
    }

    @Test
    fun GIVEN_entry_WHEN_with_entry_case_sensitive_THEN_filters_case_sensitively() {
        val result = names.withEntry("steve", ignoreCase = false)

        assertEquals(listOf("steve_alt"), result)
    }

    @Test
    fun GIVEN_entry_without_match_WHEN_with_entry_THEN_returns_empty_list() {
        val result = names.withEntry("herobrine")

        assertEquals(emptyList(), result)
    }

    @Test
    fun GIVEN_substring_entry_WHEN_with_entry_THEN_matches_anywhere_in_string() {
        val result = names.withEntry("e")

        assertEquals(listOf("Steve", "Alex", "steve_alt"), result)
    }

    @Test
    fun GIVEN_blank_space_entry_WHEN_with_entry_THEN_treated_as_non_empty_filter() {
        val list = listOf("a b", "cd", "e f")

        assertEquals(listOf("a b", "e f"), list.withEntry(" "))
    }

    @Test
    fun GIVEN_empty_list_WHEN_with_entry_THEN_returns_empty() {
        assertEquals(emptyList(), emptyList<String>().withEntry("anything"))
    }

    @Test
    fun GIVEN_all_items_match_WHEN_with_entry_THEN_returns_all_preserving_order_and_duplicates() {
        val list = listOf("abc", "ab", "abc")

        assertEquals(list, list.withEntry("ab"))
    }
}
