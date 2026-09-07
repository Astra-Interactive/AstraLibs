package ru.astrainteractive.astralibs.command.api.brigadier.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.tree.CommandNode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@Suppress("TestFunctionName")
class MultiplatformCommandTest {

    private val multiplatformCommand = MultiplatformCommand(FakeMultiplatformCommands())

    private fun CommandNode<Any>.child(name: String): CommandNode<Any>? {
        return children.firstOrNull { child -> child.name == name }
    }

    @Test
    fun GIVEN_literal_inside_argument_WHEN_node_built_THEN_literal_is_child_of_the_argument() {
        val node = with(multiplatformCommand) {
            command("cmd") {
                argument("name", StringArgumentType.string()) { _ ->
                    literal("force") { }
                }
            }
        }.build()

        val argument = assertNotNull(node.child("name"))
        assertNotNull(argument.child("force"))
        assertNull(node.child("force"))
    }

    @Test
    fun GIVEN_literal_inside_literal_WHEN_node_built_THEN_literal_is_child_of_the_parent_literal() {
        val node = with(multiplatformCommand) {
            command("cmd") {
                literal("sub") { }
            }
        }.build()

        assertNotNull(node.child("sub"))
    }

    @Test
    fun GIVEN_argument_with_both_runs_and_literal_WHEN_node_built_THEN_both_paths_are_executable() {
        val node = with(multiplatformCommand) {
            command("cmd") {
                argument("name", StringArgumentType.string()) { _ ->
                    runs { }
                    literal("force") { runs { } }
                }
            }
        }.build()

        val argument = assertNotNull(node.child("name"))
        assertNotNull(argument.command)
        assertNotNull(assertNotNull(argument.child("force")).command)
        assertEquals(1, node.children.size)
    }
}
