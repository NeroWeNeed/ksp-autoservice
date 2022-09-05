plugins {
    kotlin("multiplatform") version "1.7.10" apply false
    id("com.google.devtools.ksp") version "1.7.10-1.0.6" apply false
}

allprojects {
    group = "github.nwn"
    version = "0.0.1-SNAPSHOT"
}