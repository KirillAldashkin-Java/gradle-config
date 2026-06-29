package local.runner

import local.currentClassPath
import local.deserializeString
import local.findClasses
import java.lang.reflect.Modifier
import java.nio.file.Path

interface Runner {
    fun run()
}

fun main(args: Array<String>) {
    val config = deserializeString(args[0]) as RunnerConfig

    config.source.sets.values
        .flatMap { it.directories.values }
        .forEach { it.output.mkdirs() }

    fun checkClass(name: String): Class<*>? {
        val klass = runCatching { Class.forName(name) }.getOrNull() ?: return null
        if (!Runner::class.java.isAssignableFrom(klass)) return null
        if (klass.isMemberClass || klass.isLocalClass) return null
        if (klass.isInterface || Modifier.isAbstract(klass.modifiers)) return null
        return klass
    }

    fun run(klass: Class<*>) {
        println("Running $klass")
        val constructor = klass.getConstructor(RunnerConfig::class.java)
        (constructor.newInstance(config) as Runner).run()
    }

    currentClassPath
        .flatMap(Path::findClasses)
        .mapNotNull(::checkClass)
        .forEach(::run)
}
