plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp")
}

repositories {
    mavenCentral()
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
                implementation(project(":annotations"))
                implementation(kotlin("test"))
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(project(":annotations"))
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
    add("kspJvm", project(":processor"))
    add("kspJvmTest", project(":processor"))
}