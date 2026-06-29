package local

import local.poet.Java
import local.poet.Kotlin
import local.runner.RunnerConfig
import java.io.FileOutputStream

class Generator(val set: RunnerConfig.SourceSet, val pkg: String) {
    fun nest(name: String) = Generator(set, if (pkg.isEmpty()) name else "$pkg.$name")

    fun javaFile(type: Java.TypeSpec): Java.File {
        val file = Java.File.builder(pkg, type).build()
        set.java.output.mkdirs()
        file.writeTo(set.java.output)
        return file
    }

    fun kotlinFile(name: String, builder: Kotlin.FileBuilder.() -> Unit): Kotlin.File {
        val file = Kotlin.File.builder(pkg, name).apply(builder).build()
        set.kotlin.output.mkdirs()
        file.writeTo(set.kotlin.output)
        return file
    }

    fun resourceFile(name: String, writer: (FileOutputStream) -> Unit): String {
        set.resources.output.mkdirs()
        val path = set.resources.output.resolve(name)
        path.outputStream().use(writer)
        return path.relativeTo(set.resources.output).path
    }

    fun javaName(name: String): Java.ClassName = Java.ClassName.get(pkg, name)

    fun kotlinName(name: String) = Kotlin.ClassName(pkg, name)
}

fun RunnerConfig.SourceSet.generator() = Generator(this, "")
