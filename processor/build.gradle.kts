plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}
kotlin {
    jvm {
        withJava()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":annotations"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.symbol.processing.api)
                implementation(libs.google.auto.service)
                implementation(libs.kotlinpoet)
            }
        }
        all {
            languageSettings {
                optIn("github.nwn.auto.service.AutoServiceSymbolProcessor")
            }
        }
    }

}