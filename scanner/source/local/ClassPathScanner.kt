package local

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import kotlin.io.path.Path
import kotlin.streams.asSequence

val currentClassPath = System.getProperty("java.class.path")
    .splitToSequence(File.pathSeparatorChar)
    .map(::Path)
    .toList()

private fun Sequence<String>.process(sep: Char): Sequence<String> = this
    .filter { it.endsWith(".class") }
    .map { it.substring(0, it.length - 6).replace(sep, '.') }
    .toList()
    .asSequence()

fun Path.findClasses(): Sequence<String> {
    if (!Files.exists(this)) return sequenceOf()

    if (Files.isDirectory(this)) {
        return this.let(Files::walk).use { it
            .asSequence()
            .filter(Files::isRegularFile)
            .map(this::relativize)
            .map(Path::toString)
            .process(File.separatorChar)
        }
    }

    if (this.toString().endsWith(".jar")) {
        return ZipFile(this.toFile()).use { it
            .entries()
            .asSequence()
            .map(ZipEntry::getName)
            .process('/')
        }
    }

    return sequenceOf()
}
