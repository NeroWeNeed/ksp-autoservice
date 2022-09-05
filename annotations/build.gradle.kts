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
        val commonMain by getting
        val jvmMain by getting
    }
}