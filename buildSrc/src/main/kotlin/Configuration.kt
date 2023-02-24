import java.text.SimpleDateFormat

const val rootName = "Invero"
const val rootGroup = "cc.trixey.invero"
const val rootVersion = "1.0.0-canary-10"

const val kotlinVersion = "1.8.10"
const val shadowJarVersion = "7.1.2"
const val taboolibPluginVersion = "1.56"
val taboolibVersion = taboolibLatestVersion.also { println("Using taboolib-version = $it") }
const val repoTabooProject = "https://repo.tabooproject.org/repository/releases"

val isoInstantFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

val usedTaboolibModules = setOf(
    "common",
    "common-5",
    "platform-bukkit",
    "module-nms",
    "module-nms-util",
    "module-kether",
    "module-configuration",
    "module-lang",
    "module-chat",
    "module-database",
    "expansion-javascript",
    "expansion-player-database",
)