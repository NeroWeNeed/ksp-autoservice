plugins {
    kotlin("multiplatform")
    `maven-publish`
}
kotlin {
    jvm {
        withJava()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":auto-service-annotations"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.symbol.processing.api)
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