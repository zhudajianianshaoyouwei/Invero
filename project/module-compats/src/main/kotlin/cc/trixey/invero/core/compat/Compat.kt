package cc.trixey.invero.core.compat

import cc.trixey.invero.common.Invero
import cc.trixey.invero.common.supplier.ItemSourceProvider
import cc.trixey.invero.core.geneartor.BaseGenerator
import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.platform.Awake
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

    override fun visitEnd(clazz: Class<*>, instance: Supplier<*>?) {
        if (clazz.isAnnotationPresent(DefItemProvider::class.java)) {
            val annotation = clazz.getAnnotation(DefItemProvider::class.java)
            val provider = (instance?.get() ?: clazz.getConstructor().newInstance()) as ItemSourceProvider
            if (provider is PluginHook && !provider.isHooked) return

            annotation.namespaces.forEach { Invero.API.registerItemSourceProvider(it, provider) }
        } else if (clazz.isAnnotationPresent(DefGeneratorProvider::class.java)) {
            val annotation = clazz.getAnnotation(DefGeneratorProvider::class.java)
            val generator = clazz.getConstructor().newInstance() as BaseGenerator
            val namespace = annotation.namespace
            val id = annotation.id

            Invero.API.registerElementGenerator(namespace, id, generator)
        }
    }

    override fun getLifeCycle() = LifeCycle.ACTIVE

}