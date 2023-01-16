@file:RuntimeDependencies(
    RuntimeDependency("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.4.1"),
    RuntimeDependency("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.4.1")
)
@file:OptIn(ExperimentalSerializationApi::class)


package cc.trixey.invero.serialize

import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Menu
import cc.trixey.invero.core.panel.PanelStandard
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type

/**
 * Invero
 * cc.trixey.invero.serialize.MenuSerializer
 *
 * @author Arasple
 * @since 2023/1/16 14:02
 */


private val module = SerializersModule {
    polymorphic(AgentPanel::class) {
        subclass(PanelStandard::class)
    }
}

private val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    explicitNulls = false
    serializersModule = module
}


fun Configuration.toMenu(): Menu {
    changeType(Type.JSON)
    return json.decodeFromString(saveToString())
}

fun Menu.toJson(): String {
    return json.encodeToString(value = this)
}