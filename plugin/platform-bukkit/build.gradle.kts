plugins {
    id("io.izzel.taboolib") version taboolibPluginVersion
}

group = rootGroup

taboolib {
    version = taboolibVersion

    install("common")
    install("common-5")
    install("module-nms")
    install("module-nms-util")
    install("module-kether")
    install("module-configuration")
    install("module-lang")
    install("module-chat")
    install("module-database")
    install("expansion-javascript")
    install("expansion-player-database")
    install("platform-bukkit")

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
    includeEmptyDirs = false
    archiveBaseName.set("$rootName-$rootVersion")
}