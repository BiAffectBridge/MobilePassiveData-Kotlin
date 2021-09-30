package org.sagebionetworks.assessmentmodel.passivedata.recorder.motion

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import org.sagebionetworks.assessmentmodel.passivedata.ResultData
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionConfiguration
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionStatus
import org.sagebionetworks.assessmentmodel.passivedata.recorder.Recorder

abstract class SharedFlowRecorder<in E, out R : ResultData>(
    open val identifier: String,
    override val configuration: AsyncActionConfiguration,
    open val scope: CoroutineScope,
    private val flow: SharedFlow<E>
) :
    Recorder<R> {
    internal var _asyncStatus = AsyncActionStatus.IDLE
    override val status: AsyncActionStatus
        get() = _asyncStatus

    private var job: Job? = null

    override fun start() {
        if (_asyncStatus >= AsyncActionStatus.STARTING) {
            Napier.e("Recorder $identifier has already been started")
            return
        }
        _asyncStatus = AsyncActionStatus.RUNNING
        job = scope.launch {
            flow
                .onCompletion {
                    _asyncStatus = AsyncActionStatus.WAITING_TO_STOP
                    completedHandlingFlow(it)
                }
                .collect { handleElement(it) }
        }
    }

    abstract suspend fun handleElement(e: E)

    abstract fun completedHandlingFlow(e: Throwable?)

    override fun stop() {
        job?.cancel()
        job = null
    }

    override fun cancel() {
        job?.cancel()
        job = null
        _asyncStatus = AsyncActionStatus.CANCELLED
    }
}