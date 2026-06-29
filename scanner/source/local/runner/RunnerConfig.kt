package local.runner

import java.io.File
import java.io.Serializable

class RunnerConfig(val source: SourceSetContainer): Serializable {
    class SourceSetContainer(val sets: Map<String, SourceSet>) : Serializable, Map<String, SourceSet> by sets {
        val main: SourceSet get() = this["main"]!!
        val test: SourceSet get() = this["test"]!!
    }

    class SourceSet(
        val compileClasspath: Set<File>,
        val runtimeClasspath: Set<File>,
        val directories: Map<String, SourceDirectorySet>
    ) : Serializable, Map<String, SourceDirectorySet> by directories {
        val java: SourceDirectorySet get() = this["java"]!!
        val kotlin: SourceDirectorySet get() = this["kotlin"]!!
        val resources: SourceDirectorySet get() = this["resources"]!!
    }

    class SourceDirectorySet(
        val inputs: Set<File>,
        val includes: Set<String>,
        val output: File
    ) : Serializable
}