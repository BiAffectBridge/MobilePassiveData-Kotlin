package org.sagebionetworks.assessmentmodel.passivedata.asyncaction

import kotlinx.serialization.SerialName
import org.sagebionetworks.assessmentmodel.passivedata.internal.StringEnum

enum class AsyncActionStatus(override val serialName: String) : StringEnum {
    /// Initial state before the controller has been started.
    IDLE("idle"),

    /// Status if the controller is currently requesting authorization. Once in this state and
    /// until the controller is `starting`, the UI should be blocked from any view transitions.
    REQUESTING_PERMISSION("requestingPermission"),

    /// Status if the controller has granted permission, but not yet been started.
    PERMISSION_GRANTED("permissionGranted"),

    /// The controller is starting up. This is the state once `AsyncAction.start()` has been
    /// called but before the recorder or request is running.
    STARTING("starting"),

    /// The action is running. For `RecorderConfiguration` controllers, this means that the
    /// recording is open. For `RequestConfiguration` controllers, this means that the request is
    /// in-flight.
    RUNNING("running"),

    /// Waiting for in-flight buffers to be appended and ready to close.
    WAITING_TO_STOP("waitingToStop"),

    /// Cleaning up by closing any buffers or file handles and processing any results that are
    /// returned by this controller.
    PROCESSING_RESULTS("processingResults"),

    /// Stopping any sensor managers. The controller should move to this state **after** any
    /// results are processed.
    /// - note: Once in this state, the async action should **not** be changing the results
    /// associated with this action.
    STOPPING("stopping"),

    /// The controller is finished running and ready to `dealloc`.
    FINISHED("finished"),

    /// The recorder or request was cancelled and any results may be invalid.
    CANCELLED("cancelled"),

    /// The recorder or request failed and any results may be invalid.
    FAILED("failed")
}