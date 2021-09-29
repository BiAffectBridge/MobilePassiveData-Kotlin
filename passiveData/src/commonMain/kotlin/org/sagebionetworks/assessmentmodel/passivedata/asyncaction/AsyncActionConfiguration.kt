package org.sagebionetworks.assessmentmodel.passivedata.asyncaction

/**
 *  `AsyncActionConfiguration` defines general configuration for an asynchronous background action
 * that should be run in the background. Depending upon the parameters and how the action is set
 * up, this could be something that is run continuously or else is paused or reset based on a
 * timeout interval.
 *
 * The configuration is intended to be a serializable object and does not call services, record
 * data, or anything else.
 *
 * - seealso: `AsyncActionController`.
 *
 */


interface AsyncActionConfiguration {
    /// A short string that uniquely identifies the asynchronous action within the task.
    val identifier: String

    /// The type of the async action.
    val typeName: String

    /// An identifier marking the step at which to start the action. If `nil`, then the action will
    /// be started when the task is started.
    val startStepIdentifier: String?


    /// Validate the async action to check for any configuration that should throw an error.
    // TODO: fun validate()
}