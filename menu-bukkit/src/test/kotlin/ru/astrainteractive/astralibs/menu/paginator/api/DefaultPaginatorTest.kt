package ru.astrainteractive.astralibs.menu.paginator.api

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@Suppress("TestFunctionName")
class DefaultPaginatorTest {

    @Test
    fun GIVEN_constructor_args_WHEN_created_THEN_initial_context_reflects_them() {
        val paginator = DefaultPaginator(page = 1, maxItems = 20, maxItemsPerPage = 5)

        val context = paginator.paginatorContextStateFlow.value

        assertEquals(1, context.page)
        assertEquals(20, context.maxItems)
        assertEquals(5, context.maxItemsPerPage)
    }

    @Test
    fun GIVEN_page_within_bounds_WHEN_open_page_THEN_page_updated() {
        val paginator = DefaultPaginator(page = 0, maxItems = 20, maxItemsPerPage = 5)

        paginator.openPage(3)

        assertEquals(3, paginator.paginatorContextStateFlow.value.page)
    }

    @Test
    fun GIVEN_page_equal_to_max_pages_WHEN_open_page_THEN_allowed() {
        val paginator = DefaultPaginator(page = 0, maxItems = 20, maxItemsPerPage = 5)

        paginator.openPage(4)

        assertEquals(4, paginator.paginatorContextStateFlow.value.page)
    }

    @Test
    fun GIVEN_page_above_max_pages_WHEN_open_page_THEN_throws() {
        val paginator = DefaultPaginator(page = 0, maxItems = 20, maxItemsPerPage = 5)

        assertFailsWith<IllegalStateException> { paginator.openPage(5) }
    }

    @Test
    fun GIVEN_negative_page_WHEN_open_page_THEN_throws() {
        val paginator = DefaultPaginator(page = 0, maxItems = 20, maxItemsPerPage = 5)

        assertFailsWith<IllegalStateException> { paginator.openPage(-1) }
    }

    @Test
    fun GIVEN_failed_open_page_WHEN_thrown_THEN_state_unchanged() {
        val paginator = DefaultPaginator(page = 2, maxItems = 20, maxItemsPerPage = 5)

        assertFailsWith<IllegalStateException> { paginator.openPage(99) }

        assertEquals(2, paginator.paginatorContextStateFlow.value.page)
    }

    @Test
    fun GIVEN_update_block_WHEN_applied_THEN_context_transformed() {
        val paginator = DefaultPaginator(page = 0, maxItems = 20, maxItemsPerPage = 5)

        paginator.update { context -> context.copy(maxItems = 50) }

        assertEquals(50, paginator.paginatorContextStateFlow.value.maxItems)
    }

    @Test
    fun GIVEN_fewer_items_than_page_size_WHEN_open_first_page_THEN_allowed() {
        val paginator = DefaultPaginator(page = 0, maxItems = 3, maxItemsPerPage = 5)

        paginator.openPage(0)

        assertEquals(0, paginator.paginatorContextStateFlow.value.page)
    }

    @Test
    fun GIVEN_fewer_items_than_page_size_WHEN_open_second_page_THEN_throws() {
        val paginator = DefaultPaginator(page = 0, maxItems = 3, maxItemsPerPage = 5)

        assertFailsWith<IllegalStateException> { paginator.openPage(1) }
    }

    @Test
    fun GIVEN_updated_bounds_WHEN_open_newly_valid_page_THEN_allowed() {
        val paginator = DefaultPaginator(page = 0, maxItems = 5, maxItemsPerPage = 5)
        assertFailsWith<IllegalStateException> { paginator.openPage(4) }

        paginator.update { context -> context.copy(maxItems = 25) }
        paginator.openPage(4)

        assertEquals(4, paginator.paginatorContextStateFlow.value.page)
    }

    @Test
    fun GIVEN_same_page_WHEN_open_page_THEN_idempotent() {
        val paginator = DefaultPaginator(page = 2, maxItems = 50, maxItemsPerPage = 5)

        paginator.openPage(2)

        assertEquals(2, paginator.paginatorContextStateFlow.value.page)
    }

    @Test
    fun GIVEN_no_arg_paginator_WHEN_open_page_THEN_throws_arithmetic_from_zero_page_size() {
        val paginator = DefaultPaginator()

        assertFailsWith<ArithmeticException> { paginator.openPage(0) }
    }

    @Test
    fun GIVEN_update_WHEN_applied_THEN_other_fields_preserved() {
        val paginator = DefaultPaginator(page = 1, maxItems = 20, maxItemsPerPage = 5)

        paginator.update { context -> context.copy(maxItems = 40) }

        val context = paginator.paginatorContextStateFlow.value
        assertEquals(1, context.page)
        assertEquals(40, context.maxItems)
        assertEquals(5, context.maxItemsPerPage)
    }

    @Test
    fun GIVEN_not_last_page_WHEN_open_next_page_THEN_advances() {
        val paginator = DefaultPaginator(page = 0, maxItems = 20, maxItemsPerPage = 5)

        paginator.openNextPage()

        assertEquals(1, paginator.context.page)
    }

    @Test
    fun GIVEN_last_page_WHEN_open_next_page_THEN_throws() {
        val paginator = DefaultPaginator(page = 4, maxItems = 20, maxItemsPerPage = 5)

        assertFailsWith<IllegalStateException> { paginator.openNextPage() }
    }

    @Test
    fun GIVEN_not_first_page_WHEN_open_prev_page_THEN_goes_back() {
        val paginator = DefaultPaginator(page = 2, maxItems = 20, maxItemsPerPage = 5)

        paginator.openPrevPage()

        assertEquals(1, paginator.context.page)
    }

    @Test
    fun GIVEN_first_page_WHEN_open_prev_page_THEN_throws() {
        val paginator = DefaultPaginator(page = 0, maxItems = 20, maxItemsPerPage = 5)

        assertFailsWith<IllegalStateException> { paginator.openPrevPage() }
    }

    @Test
    fun GIVEN_paginator_WHEN_context_accessed_THEN_returns_state_flow_value() {
        val paginator = DefaultPaginator(page = 3, maxItems = 20, maxItemsPerPage = 5)

        assertEquals(paginator.paginatorContextStateFlow.value, paginator.context)
    }

    @Test
    fun GIVEN_paginator_WHEN_set_max_items_THEN_updates_and_preserves_other_fields() {
        val paginator = DefaultPaginator(page = 1, maxItems = 20, maxItemsPerPage = 5)

        paginator.setMaxItems(60)

        val context = paginator.context
        assertEquals(60, context.maxItems)
        assertEquals(1, context.page)
        assertEquals(5, context.maxItemsPerPage)
    }

    @Test
    fun GIVEN_paginator_WHEN_set_max_items_per_page_THEN_updates_field() {
        val paginator = DefaultPaginator(page = 0, maxItems = 20, maxItemsPerPage = 5)

        paginator.setMaxItemsPerPage(10)

        assertEquals(10, paginator.context.maxItemsPerPage)
    }
}
