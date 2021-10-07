package org.sagebionetworks.assessmentmodel.passivedata.asyncaction

import org.sagebionetworks.assessmentmodel.passivedata.internal.StringEnum

/**
 * A list of standard recorders.
 */
sealed class AsyncActionType(override val name: String) : StringEnum {
    object Distance : AsyncActionType("distance")
    object Microphone : AsyncActionType("microphone")
    object Motion : AsyncActionType("motion")
    object Weather : AsyncActionType("weather")
}