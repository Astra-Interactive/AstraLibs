package ru.astrainteractive.astralibs.service

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Minimal [Service] whose only behaviour is flipping [state], used to exercise the interface itself. */
class FakeService : Service {
    private val mutableState = MutableStateFlow(Service.State.Idle)
    override val state: StateFlow<Service.State> = mutableState.asStateFlow()

    val transitions = mutableListOf<Service.State>()

    override fun onEnable() {
        mutableState.value = Service.State.Active
        transitions.add(Service.State.Active)
    }

    override fun onDisable() {
        mutableState.value = Service.State.Idle
        transitions.add(Service.State.Idle)
    }
}
