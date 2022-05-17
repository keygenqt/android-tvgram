package com.keygenqt.tvgram.exceptions

import kotlinx.serialization.Serializable
import javax.annotation.concurrent.Immutable

@Immutable
@Serializable
data class ApiException(
    val code: Int = 0,
    override val message: String = "",
) : RuntimeException()