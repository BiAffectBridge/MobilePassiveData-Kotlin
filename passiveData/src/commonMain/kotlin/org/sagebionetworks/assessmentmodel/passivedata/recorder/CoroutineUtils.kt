package org.sagebionetworks.assessmentmodel.passivedata.recorder

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job

val coroutineExceptionLogger = CoroutineExceptionHandler { coroutineContext, throwable ->
    Napier.w("Encountered coroutine exception in job ${coroutineContext[Job]}", throwable)
}