package com.robertkomarek.novenen.annotation

import androidx.annotation.DrawableRes
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class DrawableResource(
    val value: KClass<*>
)