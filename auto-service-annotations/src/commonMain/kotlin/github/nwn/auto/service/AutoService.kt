package github.nwn.auto.service

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
@Repeatable
annotation class AutoService(vararg val value: KClass<*>)