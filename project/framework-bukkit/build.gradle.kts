dependencies {
    compileModule("framework-common")

    compileTaboo("common")
    compileTaboo("platform-bukkit")
    compileTaboo("module-nms")
    compileTaboo("module-nms-util")

    compileNMS()
    compileCore(11903)
    compileCore(11701)
    compileCore(11604, complete = true)
}

/*
cc.trixey.invero

ui (framework)
  bukkit
  common

common
  library
  animation
  action
  api (interface)

core
  api (impl)
  command
  compats
  kether
  menu
  serializer

 */

/*
framework-bukkit
framework-common

module-common (common.*)
module-core (core.*)

impled core:

1 module-api (core.api)
2 module-command (core.command)
3 module-compats (core.compats)
4 module-kether (core.kether)
 */