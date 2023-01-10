plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

subprojects {
    gradle.buildFinished { buildDir.deleteRecursively() }
}

dependencies {
    childProjects.forEach {
        implementation(it.value)
    }
}

tasks {
    withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
        archiveClassifier.set("")
        exclude("META-INF/**")
    }
    build {
        dependsOn(shadowJar)
    }
}