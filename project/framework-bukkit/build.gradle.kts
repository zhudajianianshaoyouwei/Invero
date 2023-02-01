dependencies {
    compileModule("framework-common")

    installTaboo("common")
    installTaboo("platform-bukkit")
    installTaboo("module-nms")
    installTaboo("module-nms-util")

    compileNMS()
    compileCore(11903)
    compileCore(11701)
    compileCore(11604, complete = true)
}