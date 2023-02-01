plugins {
    kotlin("plugin.serialization") version "1.7.21"
}

dependencies {
    compileModule("framework-common")
    compileModule("framework-bukkit")
    compileModule("module-common")

    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    installTaboo("common")
    installTaboo("common-5")
    installTaboo("platform-bukkit")
    installTaboo("module-configuration")
    installTaboo("module-kether")
    installTaboo("module-chat")
    installTaboo("module-database")
    installTaboo("module-nms")
    installTaboo("module-nms-util")
    installTaboo("expansion-player-database")

    compileNMS()
    compileCore(11903)
}