dependencies {
    compileModule("framework-common")
    compileModule("framework-bukkit")
    compileModule("module-common")
    compileModule("module-core")

    installTaboo("common")
    installTaboo("common-5")
    installTaboo("platform-bukkit")
    installTaboo("module-configuration")
    installTaboo("module-kether")
    installTaboo("module-nms")
    installTaboo("module-nms-util")
    installTaboo("expansion-player-database")

    compileCore(11903)
}