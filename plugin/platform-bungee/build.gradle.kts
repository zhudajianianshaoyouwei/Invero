plugins {
    id("io.izzel.taboolib") version taboolibPluginVersion
}

taboolib {
    install("common")
    install("platform-bungee")

    description {
        name = rootName

        contributors {
            name("Arasple")
        }
    }

    options("skip-minimize", "keep-kotlin-module", "skip-taboolib-relocate")
    classifier = null
    version = taboolibVersion
}