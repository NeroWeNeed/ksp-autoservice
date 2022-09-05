package github.nwn.auto.service

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

/**
 * KSP Implementation of Google's AutoService Java Annotation Processor.
 * Generates appropriate service files using either the provided [AutoService] or the Google version.
 */
class AutoServiceSymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val processGoogleAnnotations: Boolean,
    private val wrapObjects: Boolean
) :
    SymbolProcessor {
    companion object {
        /**
         * Option for processing google annotations. If false, will only process [github.nwn.auto.service.AutoService] and will ignore all Google Annotations.
         * True by default.
         */
        const val OPTION_PROCESS_GOOGLE_ANNOTATIONS = "github.nwn.auto.service.option.google_annotations"

        /**
         * Option for wrapping object service implementations. If true, Will generate proxy classes for object classes implementing services and use them as the Service Provider.
         * True by default.
         */
        const val OPTION_OBJECT_WRAPPER = "github.nwn.auto.service.option.use_object_wrapper"

        private const val GOOGLE_AUTO_SERVICE_ANNOTATION = "com.google.auto.service.AutoService"
        private const val AUTO_SERVICE_ANNOTATION = "github.nwn.auto.service.AutoService"

        const val SERVICE_PROVIDER_SUFFIX = "ProxyServiceProvider"
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val nwnSymbols = resolver.getSymbolsWithAnnotation(AUTO_SERVICE_ANNOTATION)
        val googleSymbols =
            if (processGoogleAnnotations) resolver.getSymbolsWithAnnotation(GOOGLE_AUTO_SERVICE_ANNOTATION) else emptySequence()
        val ret = ArrayList<KSAnnotated>()
        nwnSymbols.filterIsInstance<KSClassDeclaration>().pairServices(AUTO_SERVICE_ANNOTATION, ret)
            .plus(
                googleSymbols.filterIsInstance<KSClassDeclaration>().pairServices(GOOGLE_AUTO_SERVICE_ANNOTATION, ret)
            ).map { (service, implementation) ->
                if (implementation.classKind == ClassKind.OBJECT && wrapObjects) {
                    codeGenerator.createNewFile(
                        Dependencies(
                            true,
                            *(implementation.containingFile?.let { arrayOf(it) } ?: emptyArray())
                        ),
                        implementation.packageName.asString(),
                        "${implementation.qualifiedName?.getShortName()}\$$SERVICE_PROVIDER_SUFFIX",
                    ).bufferedWriter().use {
                        it.writeObjectWrapper(
                            implementation.packageName.asString(),
                            implementation.simpleName.asString(),
                            service.declaration.packageName.asString(),
                            service.declaration.simpleName.asString()
                        )
                    }.let {
                        ServiceRequest(
                            service.declaration.qualifiedName?.asString(),
                            it,
                            implementation.containingFile
                        )
                    }
                } else {
                    ServiceRequest(
                        service.declaration.qualifiedName?.asString(),
                        implementation.qualifiedName?.asString(),
                        implementation.containingFile
                    )
                }
            }.groupBy {
                it.serviceClass
            }.forEach { (service, requests) ->
                if (service != null) {
                    codeGenerator.createNewFile(
                        Dependencies(
                            true,
                            *requests.mapNotNull { it.containingFile }.toTypedArray()
                        ),
                        "META-INF/services",
                        service,
                        extensionName = ""
                    ).bufferedWriter().use { writer ->
                        requests.forEach {
                            if (it.implementationClass != null)
                                writer.appendLine(it.implementationClass)
                        }
                    }
                }
            }
        return ret
    }
}