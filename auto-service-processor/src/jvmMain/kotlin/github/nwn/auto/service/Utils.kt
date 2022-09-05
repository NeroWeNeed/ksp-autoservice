package github.nwn.auto.service

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import java.io.Writer

/**
 * Utility function for generating proxy classes for object class services and writing to a writer.
 * @param implementationPackage The package name of the Service Implementation class.
 * @param implementationClass The class name of the Service Implementation class.
 * @param servicePackage The package name of the Service Interface class.
 * @param serviceClass The class name of the Service Interface class.
 * @return The fully qualified name of the newly created class.
 */
fun Writer.writeObjectWrapper(
    implementationPackage: String,
    implementationClass: String,
    servicePackage: String,
    serviceClass: String
): String {
    val type = TypeSpec.classBuilder("$implementationClass\$${AutoServiceSymbolProcessor.SERVICE_PROVIDER_SUFFIX}")
        .addModifiers(KModifier.INTERNAL)
        .addSuperinterface(
            ClassName(servicePackage, serviceClass),
            CodeBlock.of("%T", ClassName.bestGuess("$implementationPackage.$implementationClass"))
        ).build()
    FileSpec.builder(
        implementationPackage,
        "$implementationClass\$${AutoServiceSymbolProcessor.SERVICE_PROVIDER_SUFFIX}"
    ).addType(
        type
    ).build().writeTo(this)
    return "$implementationPackage.$implementationClass\$${AutoServiceSymbolProcessor.SERVICE_PROVIDER_SUFFIX}"
}

/**
 * Utility function for pairing service interface type names to service implementations.
 * @param annotationName Name of the annotation to compare against. This is a fragile way of checking if the annotation is the included [AutoService] or the Google version if enabled. Only pass in names of annotations that match that signature.
 * @param invalidSymbols Mutable list for storing all symbols that cannot be validated.
 * @return A sequence of [KSType] and [KSClassDeclaration] pairs, where each pair represents a service interface type and the implementing class declaration.
 */
fun Sequence<KSClassDeclaration>.pairServices(annotationName: String, invalidSymbols: MutableList<KSAnnotated>) =
    flatMap { symbol ->
        if (!symbol.validate()) {
            invalidSymbols.add(symbol)
            return@flatMap emptySequence()
        }
        symbol.annotations.filter { it.annotationType.resolve().declaration.qualifiedName?.asString() == annotationName }
            .flatMap { ksAnnotation ->
                @Suppress("UNCHECKED_CAST")
                (ksAnnotation.arguments.first().value as List<KSType>).map { ksType ->
                    ksType to symbol
                }
            }
    }

internal data class ServiceRequest(
    val serviceClass: String?,
    val implementationClass: String?,
    val containingFile: KSFile?
)