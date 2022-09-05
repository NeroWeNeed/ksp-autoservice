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
                implementation("com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")
                implementation("com.google.auto.service:auto-service:1.0.1")
                implementation("com.squareup:kotlinpoet:1.12.0")
            }
        }
        all {
            languageSettings {
                optIn("github.nwn.auto.service.AutoServiceSymbolProcessor")
            }
        }
    }

}