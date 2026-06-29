package local.gradle.runner

import local.gradle.java
import local.gradle.kotlin
import local.runner.RunnerConfig
import org.gradle.api.Project
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import java.io.File

fun mapProject(project: Project, output: Provider<File>, fromName: String): RunnerConfig {
    fun SourceSetContainer.container(): RunnerConfig.SourceSetContainer {
        fun SourceSet.sourceSet(): RunnerConfig.SourceSet {
            fun SourceDirectorySet.directorySet(name: String): RunnerConfig.SourceDirectorySet {
                val output = output.map { it.resolve("${this@sourceSet.name}/$name") }

                project.tasks.findByName(this@sourceSet.getCompileTaskName(name))?.inputs?.dir(output)
                this@sourceSet.compileClasspath.files.add(output.get())
                this@sourceSet.runtimeClasspath.files.add(output.get())

                val ret = RunnerConfig.SourceDirectorySet(srcDirs, includes, output.get())
                srcDir(output)
                return ret
            }

            return RunnerConfig.SourceSet(
                this.compileClasspath.files,
                this.runtimeClasspath.files,
                mapOf(
                    Pair("java", this.java.directorySet("java")),
                    Pair("kotlin", this.kotlin.directorySet("kotlin")),
                    Pair("resources", this.resources.directorySet("resources"))
                )
            )
        }

        return RunnerConfig.SourceSetContainer(
            this.filter { it.name != fromName }.associate { Pair(it.name, it.sourceSet()) }
        )
    }

    return RunnerConfig(
        project.java.sourceSets.container()
    )
}