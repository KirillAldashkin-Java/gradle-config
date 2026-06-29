package local.gradle.runner

import local.gradle.gen
import local.gradle.java
import local.gradle.DepVersions
import local.gradle.LocalPlugin
import local.gradle.flatLayout
import local.runner.main
import local.serializeString
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import kotlin.reflect.jvm.javaMethod

fun Project.generator() {
    java {
        val gen = sourceSets.register("gen") {
            it.flatLayout()
        }

        dependencies.add("genImplementation", DepVersions["poet-java"])
        dependencies.add("genImplementation", DepVersions["poet-kotlin"])

        val klass = LocalPlugin::class.java
        val unpackGenerateJar = tasks.register("unpackGenerateJar") {
            it.outputs.file(it.temporaryDir.resolve("local-scanner.jar"))

            it.doFirst { _ ->
                klass.getResourceAsStream("/local-scanner.jar")!!.use { reader ->
                    it.outputs.files.singleFile.parentFile.mkdirs()
                    it.outputs.files.singleFile.outputStream().use { writer ->
                        reader.copyTo(writer)
                    }
                }
            }
        }

        gen.configure {
            it.compileClasspath += unpackGenerateJar.get().outputs.files
            it.runtimeClasspath += unpackGenerateJar.get().outputs.files
        }

        val generateRun = tasks.register("generateRun", JavaExec::class.java) {
            it.mainClass.set(::main.javaMethod!!.declaringClass.name)
            it.classpath += java.sourceSets.gen.runtimeClasspath
            it.classpath += unpackGenerateJar.get().outputs.files

            it.outputs.dir(it.temporaryDir.resolve("runner"))
        }

        val mapped = mapProject(this@generator, generateRun.map { it.outputs.files.singleFile }, gen.name)
        generateRun.configure {
            it.args = mutableListOf(serializeString(mapped))
        }
    }
}