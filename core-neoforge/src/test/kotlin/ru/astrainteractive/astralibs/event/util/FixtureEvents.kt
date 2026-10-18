package ru.astrainteractive.astralibs.event.util

import net.neoforged.bus.api.Event

internal class ConcreteFixtureEvent : Event()

internal abstract class ChildlessAbstractFixtureEvent : Event()

internal abstract class NestedFixtureEvent : Event() {
    class Pre : NestedFixtureEvent()
    class Post : NestedFixtureEvent()

    class UnrelatedNestedEvent : Event()

    class NotAnEvent

    abstract class Intermediate : NestedFixtureEvent() {
        class Deep : Intermediate()
    }
}
