dependencies {
    compileModule("framework-common")
    compileModule("framework-bukkit")
    compileModule("module-common")
    compileModule("module-core")
    compileModule("module-compats")

    installTaboo("common")
    installTaboo("common-5")
    installTaboo("platform-bukkit")
    installTaboo("module-kether")
    installTaboo("module-nms")
    installTaboo("module-nms-util")

    compileOnly("com.mojang:datafixerupper:4.0.26")

    compileCore(11903)
}