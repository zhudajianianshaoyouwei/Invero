package cc.trixey.invero.core.serialize.hocon

import java.io.File

/**
 * Invero
 * cc.trixey.invero.core.serialize.HoconIncluderPatch
 *
 * @author Arasple
 * @since 2023/1/14 20:17
 */
object PatchIncluder {

    private val PATTERNS = mapOf(
        Regex("include( +)\"(.+)?\"") to 2,
        Regex("include( +)(required|file)( +)?\\(( +)?\"(.+)?\"( +)?\\)") to 5,
        Regex("include( +)required( +)?\\(( +)?(file)( +)?\\(( +)?\"(.+)?\"( +)?\\)( +)?\\)") to 7,
    )

    fun patchHoconFile(file: File): String {
        val path = file.parentFile.path.replace("\\", "/")
        var fileReaded = file.readText()

        if (fileReaded.startsWith("//IGNORE_HOCON_PATCH")) {
            println("ignored")
            return fileReaded
        }

        println("path: $path")

        PATTERNS.forEach { (regex, groupIndex) ->
            fileReaded = fileReaded.replace(regex) { match ->
                val resource = match.groupValues[groupIndex]
                if (resource.shouldIgnore()) {
                    match.value
                } else {
                    "include file(\"$path/$resource\")".also {
                        println("[HOCON PATCH] [${file.name}] Changed [ ${match.value} ] to [ $it ]")
                    }
                }
            }
        }

        return fileReaded
    }

    /**
     * 是否忽略某资源内容
     */
    private fun String.shouldIgnore(): Boolean {
        return startsWith("http") || contains("/") || contains("\\")
    }


    /*
    taboolib-module-configuration

    night-config 的库对 HOCON 的处理依赖于 typesafe.config
    后者默认的 INCLUDER 对 Bukkit 平台的文件路径不能理想化兼容

    尝试覆写过程中遇到很多奇怪的问题，无奈能力有限，只能出此下策
    等有缘人出手救我
     */

    /*
    未经处理的情况下，必须要从服务端根目录开始写才能识别
    显然对用户体验很差

    include "plugins/Invero/workspace/xxx.conf"

    本插件需要实现的是，未经指定目录的情况下，默认同级目录开始
    include 语法：https://github.com/lightbend/config/blob/master/HOCON.md#includes
     */

    /*
    下面编写正则进行处理
    已知四类，且语句之间、引号和括号之间允许任意数量的空白

    resource type = file, classpath, url，我们只管 file 类别的

    include "conf.xxx" ---> 会被当 classPath，而不常用，因此这种给他套个 file
    include file("www")
    include required("")
    include required(file("xxx\" "))

    此方法显而易见的局限性就是如果 HOCON 内容中有任何被匹配的字符串，也会一并处理
     */

}