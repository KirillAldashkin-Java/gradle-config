package local.gradle

import org.gradle.api.Project
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal val Project.java get() = extensions.getByType(JavaPluginExtension::class.java)
internal fun Project.java(action: JavaPluginExtension.() -> Unit) = extensions.configure(JavaPluginExtension::class.java, action)

internal val SourceSet.kotlin: SourceDirectorySet get() = extensions.getByName("kotlin") as SourceDirectorySet

internal val SourceSetContainer.main get() = getByName("main")
internal fun SourceSetContainer.main(action: SourceSet.() -> Unit) = named("main", SourceSet::class.java, action)

internal val SourceSetContainer.test get() = getByName("test")
internal fun SourceSetContainer.test(action: SourceSet.() -> Unit) = named("test", SourceSet::class.java, action)

internal val SourceSetContainer.gen get() = getByName("gen")
internal fun SourceSetContainer.gen(action: SourceSet.() -> Unit) = named("gen", SourceSet::class.java, action)

internal fun TaskContainer.compileJava(action: JavaCompile.() -> Unit) = named("compileJava", JavaCompile::class.java, action)

internal fun TaskContainer.compileKotlin(action: KotlinCompile.() -> Unit) = named("compileKotlin", KotlinCompile::class.java, action)