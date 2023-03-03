package cc.trixey.invero.core.script.loader

import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.platform.Awake
import taboolib.library.reflex.ClassMethod
import taboolib.module.kether.KetherLoader.Companion.registerParser
import taboolib.module.kether.ScriptActionParser
import java.util.function.Supplier

/**
 * Invero
 * cc.trixey.invero.core.script.kether.loader.KetherInveroLoader
 *
 * @author Arasple
 * @since 2023/3/3 18:59
 */
@Awake
class KetherInveroLoader : ClassVisitor(0) {

    override fun visit(method: ClassMethod, clazz: Class<*>, instance: Supplier<*>?) {
        if (method.isAnnotationPresent(InveroKetherParser::class.java) && method.returnType == ScriptActionParser::class.java) {
            val parser = (if (instance == null) method.invokeStatic() else method.invoke(instance.get()))
            val annotation = method.getAnnotation(InveroKetherParser::class.java)
            val value = annotation.property<Array<String>>("name") ?: emptyArray()

            registerParser(parser as ScriptActionParser<*>, value, "invero", true)
        }
    }

    override fun getLifeCycle(): LifeCycle {
        return LifeCycle.LOAD
    }

}