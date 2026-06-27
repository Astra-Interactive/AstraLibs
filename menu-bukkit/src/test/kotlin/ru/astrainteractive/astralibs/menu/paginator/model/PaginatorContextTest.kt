package ru.astrainteractive.astralibs.menu.paginator.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class PaginatorContextTest {

    @Test
    fun GIVEN_evenly_divisible_items_WHEN_max_pages_THEN_integer_quotient() {
        val context = PaginatorContext(page = 0, maxItems = 20, maxItemsPerPage = 5)

        assertEquals(4, context.maxPages)
    }

    @Test
    fun GIVEN_remainder_items_WHEN_max_pages_THEN_truncated_quotient() {
        val context = PaginatorContext(page = 0, maxItems = 23, maxItemsPerPage = 5)

        assertEquals(4, context.maxPages)
    }

    @Test
    fun GIVEN_no_items_WHEN_max_pages_THEN_zero() {
        val context = PaginatorContext(page = 0, maxItems = 0, maxItemsPerPage = 5)

        assertEquals(0, context.maxPages)
    }

    @Test
    fun GIVEN_page_zero_WHEN_is_first_page_THEN_true() {
        val context = PaginatorContext(page = 0, maxItems = 20, maxItemsPerPage = 5)

        assertTrue(context.isFirstPage)
    }

    @Test
    fun GIVEN_non_zero_page_WHEN_is_first_page_THEN_false() {
        val context = PaginatorContext(page = 1, maxItems = 20, maxItemsPerPage = 5)

        assertFalse(context.isFirstPage)
    }

    @Test
    fun GIVEN_page_below_max_WHEN_is_last_page_THEN_false() {
        val context = PaginatorContext(page = 3, maxItems = 20, maxItemsPerPage = 5)

        assertFalse(context.isLastPage)
    }

    @Test
    fun GIVEN_page_equal_to_max_WHEN_is_last_page_THEN_true() {
        val context = PaginatorContext(page = 4, maxItems = 20, maxItemsPerPage = 5)

        assertTrue(context.isLastPage)
    }

    @Test
    fun GIVEN_page_above_max_WHEN_is_last_page_THEN_true() {
        val context = PaginatorContext(page = 5, maxItems = 20, maxItemsPerPage = 5)

        assertTrue(context.isLastPage)
    }

    @Test
    fun GIVEN_first_page_WHEN_index_of_slot_THEN_equals_slot_index() {
        val context = PaginatorContext(page = 0, maxItems = 20, maxItemsPerPage = 9)

        assertEquals(0, context.indexOfSlot(0))
        assertEquals(3, context.indexOfSlot(3))
    }

    @Test
    fun GIVEN_later_page_WHEN_index_of_slot_THEN_offset_by_page() {
        val context = PaginatorContext(page = 2, maxItems = 90, maxItemsPerPage = 9)

        assertEquals(18, context.indexOfSlot(0))
        assertEquals(21, context.indexOfSlot(3))
    }

    @Test
    fun GIVEN_fewer_items_than_page_size_WHEN_max_pages_THEN_zero() {
        val context = PaginatorContext(page = 0, maxItems = 3, maxItemsPerPage = 5)

        assertEquals(0, context.maxPages)
    }

    @Test
    fun GIVEN_empty_single_page_WHEN_checked_THEN_both_first_and_last() {
        val context = PaginatorContext(page = 0, maxItems = 0, maxItemsPerPage = 5)

        assertTrue(context.isFirstPage)
        assertTrue(context.isLastPage)
    }

    @Test
    fun GIVEN_zero_items_per_page_WHEN_max_pages_THEN_throws_arithmetic() {
        val context = PaginatorContext(page = 0, maxItems = 10, maxItemsPerPage = 0)

        assertFailsWith<ArithmeticException> { context.maxPages }
    }

    @Test
    fun GIVEN_negative_within_page_index_WHEN_index_of_slot_THEN_offsets_negatively() {
        val context = PaginatorContext(page = 1, maxItems = 50, maxItemsPerPage = 9)

        assertEquals(9, context.indexOfSlot(0))
        assertEquals(8, context.indexOfSlot(-1))
    }
}
