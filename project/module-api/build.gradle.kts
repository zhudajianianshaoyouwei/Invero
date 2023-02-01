plugins {
    kotlin("plugin.serialization") version "1.7.21"
}

dependencies {
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    compileModule("module-common")
    compileModule("module-core")

    installTaboo("common")
    installTaboo("common-5")
    installTaboo("platform-bukkit")
    installTaboo("module-configuration")
    installTaboo("module-database")
    installTaboo("module-lang")
    installTaboo("expansion-player-database")

    compileCore(11903)
}