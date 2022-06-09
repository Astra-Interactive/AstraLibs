import com.astrainteractive.astralibs.FileManager
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream
import com.google.gson.Gson
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.serializer
import java.io.File
@Serializable
data class ActionInventoriesHolder(
    val action_inventory: Map<String, ActionInventory>
) {
    companion object {
        fun getAll(): ActionInventoriesHolder? {
            val s = File("src/test/resources/action_inventory.yml")
            val yaml = Yaml(Yaml.Companion.default.serializersModule,configuration = Yaml.Companion.default.configuration.copy(strictMode = false))
            return yaml.decodeFromString(serializer(ActionInventoriesHolder::class.java),s.readText()) as? ActionInventoriesHolder
        }
    }
}

@Serializable
data class ActionInventory(
    val title: String,
    val items: Map<String, ActionInventoryItem>
)


@Serializable
data class ActionInventoryItem(
    val name: String? = null,
    val description: List<String>? = null,
    val copyInto: List<Int>? = null,
    val index: Int,
    val type: String,
    val customModelData: Int? = null,
    val enumAction: String? = null,
    val action: ActionInventoryAction? = null
)


@Serializable
data class ActionInventoryAction(
    val conditions: ActionInventoryActionCondition? = null
)


@Serializable
data class ActionInventoryActionCondition(
    val placedItems: Map<String, PlacedItem>? = null
)


@Serializable
data class PlacedItem(
    val item: String,
    val amount: Int,
    val index: Int
)