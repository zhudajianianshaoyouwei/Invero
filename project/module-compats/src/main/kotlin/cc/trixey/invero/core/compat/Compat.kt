package cc.trixey.invero.core.compat

import cc.trixey.invero.common.Invero
import cc.trixey.invero.common.supplier.ItemSourceProvider
import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.platform.Awake
import taboolib.library.reflex.ClassMethod
import java.util.function.Supplier

/**
 * Invero
 * cc.trixey.invero.core.compat.Compat
 *
 * @author Arasple
 * @since 2023/1/29 15:43
 */
@Awake
class Compat : ClassVisitor(0) {

    override fun visit(method: ClassMethod, clazz: Class<*>, instance: Supplier<*>?) {
        if (clazz.isAnnotationPresent(DefItemProvider::class.java)) {
            val annotation = clazz.getAnnotation(DefItemProvider::class.java)
            val provider = clazz.getConstructor().newInstance() as ItemSourceProvider
            if (provider is PluginHook && !provider.isHooked) return
            annotation.namespaces.forEach {
                Invero.API.registerItemSourceProvider(it, provider)
            }
        }
        super.visit(method, clazz, instance)
    }

    override fun getLifeCycle() = LifeCycle.LOAD

}