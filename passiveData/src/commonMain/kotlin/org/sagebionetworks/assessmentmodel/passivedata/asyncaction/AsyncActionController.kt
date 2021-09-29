package org.sagebionetworks.assessmentmodel.passivedata.asyncaction

import org.sagebionetworks.assessmentmodel.passivedata.ResultData
import kotlinx.coroutines.Deferred

interface AsyncActionController<R : ResultData> {

    /// The status of the controller.
    val status: AsyncActionStatus

    /// The current `stepPath` to record to log samples.
    val currentStepPath: String

    /// Results for this action controller.

    /// The configuration used to set up the controller.
    val configuration: AsyncActionConfiguration

    /**
     * Start the asynchronous action with the given completion handler.
     */
    fun start()

    /**
     * Pause the action, if applicable.
     */
    fun pause()

    /**
     * Resume the paused action, if applicable.
     */
    fun resume()

    /**
     * @return whether the action is currently paused
     */
    fun isPaused(): Boolean

    /**
     * Stop the action.
     */
    fun stop()

    /**
     * @return result for the action
     */
    val result: Deferred<R?>

    /**
     * Cancel the action.
     */
    fun cancel()
}