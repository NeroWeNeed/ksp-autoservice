rootProject.name = "ksp-autoservice"

include(":auto-service-processor")
include(":auto-service-annotations")
include(":test")

enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("symbol-processing-api", "com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")
            library("google-auto-service", "com.google.auto.service:auto-service:1.0.1")
            library("kotlinpoet", "com.squareup:kotlinpoet:1.12.0")
        }
    }
}
