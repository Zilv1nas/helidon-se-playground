package com.github.zilv1nas.config.tracing

data class TracingCallbacks(
    val onSuccess: () -> Unit,
    val onFailure: (Throwable) -> Unit,
)