package ru.astrainteractive.astralibs.menu.layout

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class InventoryLayoutTest {

    @Test
    fun GIVEN_rows_WHEN_built_THEN_flattened_in_row_order() {
        val layout = inventoryLayout<Char, String> {
            row('a', 'b', 'a')
            row('c', 'c', 'a')
        }

        assertEquals(6, layout.size)
        assertEquals('a', layout.keyAt(0))
        assertEquals('b', layout.keyAt(1))
        assertEquals('c', layout.keyAt(3))
    }

    @Test
    fun GIVEN_layout_WHEN_indices_of_key_THEN_returns_all_matching_indices() {
        val layout = inventoryLayout<Char, String> {
            row('a', 'b', 'a')
            row('c', 'c', 'a')
        }

        assertEquals(listOf(0, 2, 5), layout.indicesOf('a'))
        assertEquals(listOf(3, 4), layout.indicesOf('c'))
    }

    @Test
    fun GIVEN_layout_WHEN_count_key_THEN_returns_occurrence_count() {
        val layout = inventoryLayout<Char, String> {
            row('a', 'b', 'a')
            row('c', 'c', 'a')
        }

        assertEquals(3, layout.count('a'))
        assertEquals(1, layout.count('b'))
    }

    @Test
    fun GIVEN_layout_WHEN_first_index_of_present_key_THEN_returns_first_index() {
        val layout = inventoryLayout<Char, String> {
            row('a', 'b', 'a')
            row('c', 'c', 'a')
        }

        assertEquals(0, layout.firstIndexOf('a'))
        assertEquals(3, layout.firstIndexOf('c'))
    }

    @Test
    fun GIVEN_layout_WHEN_first_index_of_missing_key_THEN_throws() {
        val layout = inventoryLayout<Char, String> {
            row('a', 'b')
        }

        assertFailsWith<IllegalStateException> { layout.firstIndexOf('z') }
    }

    @Test
    fun GIVEN_layout_WHEN_missing_key_queried_THEN_returns_empty_and_zero() {
        val layout = inventoryLayout<Char, String> {
            row('a', 'b')
        }

        assertEquals(emptyList(), layout.indicesOf('z'))
        assertEquals(0, layout.count('z'))
    }

    @Test
    fun GIVEN_uniform_row_WHEN_built_with_size_THEN_repeats_key() {
        val layout = inventoryLayout<Char, String> {
            row(size = 3, key = 'x')
        }

        assertEquals(3, layout.size)
        assertEquals(listOf(0, 1, 2), layout.indicesOf('x'))
    }

    @Test
    fun GIVEN_rows_of_different_widths_WHEN_built_THEN_throws() {
        assertFailsWith<IllegalArgumentException> {
            inventoryLayout<Char, String> {
                row('a', 'b')
                row('c')
            }
        }
    }

    @Test
    fun GIVEN_no_rows_WHEN_built_THEN_empty_layout() {
        val layout = inventoryLayout<Char, String> { }

        assertEquals(0, layout.size)
        assertTrue(layout.indicesOf('a').isEmpty())
    }

    @Test
    fun GIVEN_layout_WHEN_map_slots_not_null_THEN_filters_null_results() {
        val layout = inventoryLayout<Char, String> {
            row('a', 'b', 'a')
        }

        val result = layout.mapSlotsNotNull('a') { index ->
            if (index == 0) "slot$index" else null
        }

        assertEquals(listOf("slot0"), result)
    }

    @Test
    fun GIVEN_layout_WHEN_map_slots_not_null_indexed_THEN_provides_iteration_and_slot_index() {
        val layout = inventoryLayout<Char, String> {
            row('a', 'b', 'a', 'a')
        }

        val result = layout.mapSlotsNotNullIndexed('a') { iterationIndex, slotIndex ->
            "$iterationIndex:$slotIndex"
        }

        assertEquals(listOf("0:0", "1:2", "2:3"), result)
    }

    @Test
    fun GIVEN_layout_WHEN_key_at_out_of_bounds_THEN_throws() {
        val layout = inventoryLayout<Char, String> {
            row('a', 'b')
        }

        assertFailsWith<IndexOutOfBoundsException> { layout.keyAt(99) }
    }

    @Test
    fun GIVEN_all_slots_same_key_WHEN_queried_THEN_full_coverage() {
        val layout = inventoryLayout<Char, String> {
            row('a', 'a', 'a')
        }

        assertEquals(3, layout.count('a'))
        assertEquals(listOf(0, 1, 2), layout.indicesOf('a'))
        assertEquals(0, layout.firstIndexOf('a'))
    }

    @Test
    fun GIVEN_single_column_WHEN_built_THEN_each_row_contributes_one_slot() {
        val layout = inventoryLayout<Char, String> {
            row('a')
            row('b')
            row('a')
        }

        assertEquals(3, layout.size)
        assertEquals(listOf(0, 2), layout.indicesOf('a'))
        assertEquals(1, layout.firstIndexOf('b'))
    }

    @Test
    fun GIVEN_absent_key_WHEN_map_slots_not_null_THEN_transform_not_invoked() {
        val layout = inventoryLayout<Char, String> {
            row('a', 'b')
        }
        var invoked = false

        val result = layout.mapSlotsNotNull('z') { _ ->
            invoked = true
            "value"
        }

        assertTrue(result.isEmpty())
        assertFalse(invoked)
    }

    @Test
    fun GIVEN_all_transforms_null_WHEN_map_slots_not_null_THEN_empty() {
        val layout = inventoryLayout<Char, String> {
            row('a', 'a')
        }

        assertTrue(layout.mapSlotsNotNull('a') { _ -> null }.isEmpty())
    }

    @Test
    fun GIVEN_filtered_middle_slot_WHEN_map_slots_not_null_indexed_THEN_iteration_index_keeps_counting() {
        val layout = inventoryLayout<Char, String> {
            row('a', 'a', 'a')
        }

        val result = layout.mapSlotsNotNullIndexed('a') { iterationIndex, slotIndex ->
            if (iterationIndex == 1) null else "$iterationIndex:$slotIndex"
        }

        assertEquals(listOf("0:0", "2:2"), result)
    }

    @Test
    fun GIVEN_mixed_row_builders_WHEN_built_THEN_combined_in_order() {
        val layout = inventoryLayout<Char, String> {
            row('a', 'b', 'c')
            row(size = 3, key = 'x')
        }

        assertEquals(6, layout.size)
        assertEquals(listOf(3, 4, 5), layout.indicesOf('x'))
        assertEquals('b', layout.keyAt(1))
    }

    @Test
    fun GIVEN_only_empty_rows_WHEN_built_THEN_empty_layout() {
        val layout = inventoryLayout<Char, String> {
            row()
            row()
        }

        assertEquals(0, layout.size)
    }
}
