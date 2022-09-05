package github.nwn.auto.service

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class AutoServiceSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        AutoServiceSymbolProcessor(
            environment.codeGenerator,
            environment.options[AutoServiceSymbolProcessor.OPTION_PROCESS_GOOGLE_ANNOTATIONS]?.toBoolean() ?: true,
            environment.options[AutoServiceSymbolProcessor.OPTION_OBJECT_WRAPPER]?.toBoolean() ?: true,
        )
}