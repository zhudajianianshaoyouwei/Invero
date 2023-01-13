dependencies {
    childProjects.forEach {
        implementation(it.value)
    }
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}

subprojects {
    gradle.buildFinished {
        buildDir.deleteRecursively()
    }
}