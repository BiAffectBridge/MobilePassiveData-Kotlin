package org.sagebionetworks.assessmentmodel.passivedata.recorder.motion

import kotlinx.coroutines.Deferred
import org.sagebionetworks.assessmentmodel.passivedata.ResultData
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionConfiguration
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionStatus
//
//class MotionRecorder<R : ResultData> : SharedFlowRecorder<R>() {
//    override val status: AsyncActionStatus
//        get() = TODO("Not yet implemented")
//    override val currentStepPath: String
//        get() = TODO("Not yet implemented")
//    override val configuration: AsyncActionConfiguration
//        get() = TODO("Not yet implemented")
//
//    override fun start() {
//        TODO("Not yet implemented")
//    }
//
//    override fun pause() {
//        TODO("Not yet implemented")
//    }
//
//    override fun resume() {
//        TODO("Not yet implemented")
//    }
//
//    override fun isPaused(): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun stop() {
//        TODO("Not yet implemented")
//    }
//
//    override val result: Deferred<R?>
//        get() = TODO("Not yet implemented")
//
//    override fun cancel() {
//        TODO("Not yet implemented")
//    }
//}