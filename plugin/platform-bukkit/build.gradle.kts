plugins {
    id("io.izzel.taboolib") version taboolibPluginVersion
}

taboolib {
    version = taboolibVersion

    usedTaboolibModules.forEach { install(it) }

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
    relocate("org.bstats", "$rootGroup.core.metrics.bstats")

    classifier = null
}

dependencies {
    compileCore(11903)

    taboo("net.kyori:adventure-api:4.12.0")
    taboo("net.kyori:adventure-text-minimessage:4.12.0")
    taboo("net.kyori:adventure-text-serializer-bungeecord:4.2.0")
    taboo("net.kyori:adventure-text-serializer-legacy:4.2.0")
    taboo("net.kyori:adventure-platform-api:4.2.0")
    taboo("net.kyori:adventure-platform-bukkit:4.2.0")

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