plugins {
    kotlin("multiplatform") version "1.7.10" apply false
    id("com.google.devtools.ksp") version "1.7.10-1.0.6" apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
    group = "github.nwn.ksp.auto.service"
    version = "1.0.0-SNAPSHOT"
}