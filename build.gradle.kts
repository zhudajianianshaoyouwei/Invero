plugins {
    java
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}

subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        maven(property("taboolibRepo").toString())
    }

    dependencies {
        compileOnly(kotlin("stdlib"))
    }

    if (parent != rootProject && name.startsWith("framework")) {
        createPublishing()
    }
}

fun Project.createPublishing() = publishing {
    repositories {
        maven(property("taboolibRepo").toString()) {
            credentials {
                username = property("taboolibUsername").toString()
                password = property("taboolibPassword").toString()
            }
            authentication { create<BasicAuthentication>("basic") }
        }
        mavenLocal()
    }

    publications {
        create<MavenPublication>("maven") {
            artifactId = project.name
            groupId = project.group.toString()
            version = project.version.toString()
            artifact(tasks["kotlinSourcesJar"])
            artifact(tasks["shadowJar"]) { classifier = null }
            println("> Apply \"$groupId:$artifactId:$version\"")
        }
    }
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}