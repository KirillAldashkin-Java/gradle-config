import local.gradle.javaTarget
import org.gradle.api.Project

typealias DepVersion = local.gradle.DepVersions
typealias JvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget

fun Project.javaTarget(target: JvmTarget) = javaTarget(target)

