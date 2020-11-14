import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin

plugins {
    kotlin("multiplatform")
}

val electronVersion: String by project
val openVersion: String by project
val kotlinVersion: String by project
val coroutinesVersion: String by project
val kotlinExtensionsVersion: String by project

kotlin {
    js {
        nodejs {
            compilations.all {
                kotlinOptions {
                    kotlinOptions.moduleKind = "umd"
                    kotlinOptions.sourceMap = true
                    kotlinOptions.sourceMapEmbedSources = "always"
                    kotlinOptions.metaInfo = true
                    kotlinOptions.main = "call"
                }
            }
        }
    }

    sourceSets {
        val jsMain by getting {
            kotlin.srcDir("src/main")
            dependencies {
                implementation(npm("electron", electronVersion))
                implementation(npm("open", openVersion))
                implementation("org.jetbrains.kotlin:kotlin-stdlib-js:$kotlinVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$coroutinesVersion")
                implementation("org.jetbrains:kotlin-extensions:$kotlinExtensionsVersion")
            }
        }
    }
}

tasks.named("jsNodeTest") {
    enabled = false
}

val copyResources = task("copyResources") {
    copy {
        from("src/main/resources")
        into("build/js/packages/projector-launcher-electronapp")
    }
}

tasks.named("compileKotlinJs").get().dependsOn(copyResources)

rootProject.plugins.withType<NodeJsRootPlugin> {
    rootProject.the<NodeJsRootExtension>().download = false
}