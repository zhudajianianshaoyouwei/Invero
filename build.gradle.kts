plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("io.izzel.taboolib") version "1.51"
}

group = "cc.trixey.invero"
version = "1.0-SNAPSHOT"

taboolib {
    version = "6.0.10-38"
    install("common")
    install("common-5")
    install("module-nms")
    install("platform-bukkit")
}

dependencies {
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11903:11903-minimize:mapped")
    compileOnly("ink.ptms.core:v11903:11903-minimize:universal")
}

repositories {
    mavenCentral()
}