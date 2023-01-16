dependencies {
    childProjects.forEach {
        implementation(it.value)
    }
}