dependencies {
    compileModule("framework-common")
    compileModule("framework-bukkit")

    implementation("net.kyori:adventure-api:4.12.0")
    implementation("net.kyori:adventure-text-minimessage:4.12.0")
    implementation("net.kyori:adventure-text-serializer-bungeecord:4.2.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.2.0")
    implementation("net.kyori:adventure-platform-api:4.2.0")
    implementation("net.kyori:adventure-platform-bukkit:4.2.0")

    installTaboo("common")
    installTaboo("common-5")
    installTaboo("platform-bukkit")
    installTaboo("module-configuration")
    installTaboo("module-kether")
    installTaboo("module-nms")
    installTaboo("module-nms-util")
    installTaboo("expansion-player-database")

    compileCore(11604, complete = true)
}