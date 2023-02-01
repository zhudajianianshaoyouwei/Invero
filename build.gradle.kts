plugins {
    java
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version kotlinVersion apply false
    id("com.github.johnrengelman.shadow") version shadowJarVersion apply false
}

subprojects {
    applyPlugins()

    repositories {
        projectRepositories()
    }

    initSubProject {
        publishing { createPublish(project) }
    }
}