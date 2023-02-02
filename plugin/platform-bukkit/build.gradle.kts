plugins {
    id("io.izzel.taboolib") version taboolibPluginVersion
}

taboolib {
    version = taboolibVersion

    standardTabooModules.forEach { install(it) }

    description {
        name = rootName

        contributors {
            name("Arasple")
        }

        dependencies {
            name("PlaceholderAPI").optional(true)
            name("Zaphkiel").optional(true)
            name("HeadDatabase").optional(true)
            name("Oraxen").optional(true)
            name("ItemsAdder").optional(true)
        }

    }

    relocate("kotlinx.serialization", "kotlinx_1_4_1.serialization")
    relocate("kotlinx.coroutines", "kotlinx_1_6_4.coroutines")
//    relocate("net.kyori", "$rootGroup.coroutines")

    classifier = null

}

dependencies {
    compileCore(11903)

    rootProject
        .childProjects["project"]!!
        .childProjects
        .forEach {
            taboo(it.value)
        }
}

tasks.jar {
    archiveBaseName.set(rootName)
    includeEmptyDirs = false
}