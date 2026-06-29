import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.4.0"
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
}

fun SourceSet.configure() {
    tasks.named<JavaCompile>(getCompileTaskName("java")) {
        sourceCompatibility = "9"
        targetCompatibility = "9"
        options.release.set(9)
    }
    tasks.named<KotlinCompile>(getCompileTaskName("kotlin")) {
        compilerOptions.jvmTarget = JvmTarget.JVM_9
    }
    java.srcDirs("$name/source")
    java.include("**/*.java")
    kotlin.srcDirs("$name/source")
    kotlin.include("**/*.kt")
    resources.srcDirs("$name/resource")
}

sourceSets {
    val scanner = create("scanner") {
        configure()
    }
    main {
        configure()
        runtimeClasspath += scanner.output
        compileClasspath += scanner.output
        // provide 'libs.versions.toml' data to dependent projects
        resources.srcDir("gradle")
    }
}

dependencies {
    implementation(libs.kotlin.plugin)
    "scannerCompileOnly"(libs.poet.java)
    "scannerCompileOnly"(libs.poet.kotlin)
}

tasks {
    val scannerJar = register<Jar>("scannerJar") {
        description = "Creates JAR from 'scanner' sources."
        from(sourceSets["scanner"].output)
        archiveClassifier.set("scanner")
    }
    jar {
        // provide scanner to dependent projects as JAR
        from(scannerJar.map { it.archiveFile })
        // use scanner ourselves
        from(sourceSets["scanner"].output)
    }
}

gradlePlugin {
    plugins {
        create("local") {
            id = "local"
            implementationClass = "local.gradle.LocalPlugin"
        }
    }
}
