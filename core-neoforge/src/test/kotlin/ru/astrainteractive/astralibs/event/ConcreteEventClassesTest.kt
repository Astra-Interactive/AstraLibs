package ru.astrainteractive.astralibs.event

import kotlin.test.Test
import kotlin.test.assertEquals

internal class ConcreteEventClassesTest {

    @Test
    fun GIVEN_concrete_event_WHEN_resolved_THEN_returns_only_itself() {
        val resolvedClasses = concreteEventClasses(ConcreteFixtureEvent::class.java)

        assertEquals(listOf(ConcreteFixtureEvent::class.java), resolvedClasses)
    }

    @Test
    fun GIVEN_abstract_event_with_nested_concretes_WHEN_resolved_THEN_returns_every_concrete_descendant() {
        val resolvedClasses = concreteEventClasses(NestedFixtureEvent::class.java).toSet()

        assertEquals(
            setOf(
                NestedFixtureEvent.Pre::class.java,
                NestedFixtureEvent.Post::class.java,
                NestedFixtureEvent.Intermediate.Deep::class.java
            ),
            resolvedClasses
        )
    }

    @Test
    fun GIVEN_abstract_intermediate_event_WHEN_resolved_THEN_returns_its_concrete_children() {
        val resolvedClasses = concreteEventClasses(NestedFixtureEvent.Intermediate::class.java)

        assertEquals(listOf(NestedFixtureEvent.Intermediate.Deep::class.java), resolvedClasses)
    }

    @Test
    fun GIVEN_abstract_event_without_concrete_children_WHEN_resolved_THEN_returns_empty_list() {
        val resolvedClasses = concreteEventClasses(ChildlessAbstractFixtureEvent::class.java)

        assertEquals(emptyList(), resolvedClasses)
    }
}
