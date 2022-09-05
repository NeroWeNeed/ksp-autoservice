rootProject.name = "ksp-autoservice"

include(":auto-service-processor")
include(":auto-service-annotations")
include(":test")

enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "1.6.10")
            version("kotlin-exposed-version", "0.39.2")
            library("symbol-processing-api", "com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")
            library("google-auto-service", "com.google.auto.service:auto-service:1.0.1")
            library("kotlinpoet", "com.squareup:kotlinpoet:1.12.0")
        }
    }
}
