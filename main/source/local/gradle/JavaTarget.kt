package local.gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

fun Project.javaTarget(target: JvmTarget) {
    val value = when (target) {
        JvmTarget.JVM_1_8 -> 8
        JvmTarget.JVM_9 -> 9
        JvmTarget.JVM_10 -> 10
        JvmTarget.JVM_11 -> 11
        JvmTarget.JVM_12 -> 12
        JvmTarget.JVM_13 -> 13
        JvmTarget.JVM_14 -> 14
        JvmTarget.JVM_15 -> 15
        JvmTarget.JVM_16 -> 16
        JvmTarget.JVM_17 -> 17
        JvmTarget.JVM_18 -> 18
        JvmTarget.JVM_19 -> 19
        JvmTarget.JVM_20 -> 20
        JvmTarget.JVM_21 -> 21
        JvmTarget.JVM_22 -> 22
        JvmTarget.JVM_23 -> 23
        JvmTarget.JVM_24 -> 24
        JvmTarget.JVM_25 -> 25
        JvmTarget.JVM_26 -> 26
    }

    tasks.compileJava {
        sourceCompatibility = value.toString()
        targetCompatibility = value.toString()
        options.release.set(value)
    }

    tasks.compileKotlin {
        compilerOptions.jvmTarget.set(target)
    }
}