package ru.astrainteractive.astralibs.command.registrar

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.tree.CommandNode

/**
 * Names of the private [CommandNode] maps that hold its children. A literal lives in `children` and
 * in `literals`, and `arguments` is cleaned as well so a name is never left behind in one of them.
 */
private val CHILD_MAP_FIELDS = listOf("children", "literals", "arguments")

@Suppress("UNCHECKED_CAST")
private fun CommandNode<*>.removeFromChildMap(fieldName: String, name: String): Boolean {
    val field = runCatching { CommandNode::class.java.getDeclaredField(fieldName) }.getOrNull() ?: return false
    field.isAccessible = true
    val children = field.get(this) as? MutableMap<String, Any> ?: return false
    return children.remove(name) != null
}

/**
 * Removes the root literal [name] from this dispatcher.
 *
 * Brigadier keeps a node's children in private maps and offers no removal API, and the mod loaders
 * ship it untouched - so a command can only leave a running server's tree through reflection. This
 * deliberately does not live in the platform-agnostic command module: Paper patches `removeCommand`
 * into Brigadier and exposes it through its command map, and goes through that instead.
 *
 * @return `true` when a node was removed.
 */
fun CommandDispatcher<*>.removeCommand(name: String): Boolean {
    val rootNode: CommandNode<*> = root
    return CHILD_MAP_FIELDS
        .map { fieldName -> rootNode.removeFromChildMap(fieldName = fieldName, name = name) }
        .any { wasRemoved -> wasRemoved }
}
