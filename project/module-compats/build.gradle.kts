val taboolibVersion: String by project

repositories {
    maven("https://jitpack.io")
    maven("https://repo.rosewooddev.io/repository/public/")
}

dependencies {
    compileOnly(project(":project:module-core"))

    compileOnly("ink.ptms.core:v11903:11903-minimize:universal")

    compileOnly("com.github.oraxen:oraxen:-SNAPSHOT")
    compileOnly("ink.ptms:Zaphkiel:1.6.0")
    compileOnly("org.black_ixx:playerpoints:3.1.1")

    compileOnly("io.izzel.taboolib:common:$taboolibVersion")
    compileOnly("io.izzel.taboolib:platform-bukkit:$taboolibVersion")
    compileOnly("io.izzel.taboolib:module-nms:$taboolibVersion")
    compileOnly(fileTree("libs"))
}