plugins {
    kotlin("multiplatform")
    `maven-publish`
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

configure<PublishingExtension> {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${System.getenv("GithubUsername")}/ksp-autoservice")
            credentials {
                username = System.getenv("GithubUsername")
                password = System.getenv("GithubToken")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["kotlin"])
        }
    }
}