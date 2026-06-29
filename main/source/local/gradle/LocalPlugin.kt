package local.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import local.gradle.runner.generator

class LocalPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply("org.jetbrains.kotlin.jvm")
        target.plugins.apply("maven-publish")

        target.repositories.mavenCentral()
        target.dependencies.add("testImplementation", DepVersions["kotlin-test"])

        target.java {
            sourceSets.main(SourceSet::flatLayout)
            sourceSets.test(SourceSet::flatLayout)
        }

        target.generator()
        target.javaTarget(JvmTarget.JVM_9)
    }
}

fun SourceSet.flatLayout() {
    java.srcDir("$name/source")
    java.include("**/*.java")
    kotlin.srcDir("$name/source")
    kotlin.include("**/*.kt")
    resources.srcDir("$name/resource")
}
