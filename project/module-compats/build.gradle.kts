repositories {
    maven("https://jitpack.io")
    maven("https://repo.rosewooddev.io/repository/public/")
}

dependencies {
    compileModule("module-common")
    compileModule("module-core")

    installTaboo("common")
    installTaboo("platform-bukkit")
    installTaboo("module-nms")

    compileOnly("com.github.oraxen:oraxen:-SNAPSHOT")
    compileOnly("ink.ptms:Zaphkiel:1.6.0")
    compileOnly("org.black_ixx:playerpoints:3.1.1")

    compileCore(11903)
    compileLocal(project = project, "libs")
}