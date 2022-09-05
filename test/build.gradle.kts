plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp")
}
kotlin {
    jvm {
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(project(":auto-service-annotations"))
                implementation(kotlin("test"))
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(project(":auto-service-annotations"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.google.auto.service)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
                implementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
                implementation(kotlin("test"))
            }
        }
    }
}
dependencies {
    add("kspJvm", project(":auto-service-processor"))
    add("kspJvmTest", project(":auto-service-processor"))
}