plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}

subprojects {
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        maven("https://repo.tabooproject.org/repository/releases")
        maven("http://ptms.ink:8081/repository/releases") { isAllowInsecureProtocol = true }
    }

    dependencies { compileOnly(kotlin("stdlib")) }

}

gradle.buildFinished { buildDir.deleteRecursively() }