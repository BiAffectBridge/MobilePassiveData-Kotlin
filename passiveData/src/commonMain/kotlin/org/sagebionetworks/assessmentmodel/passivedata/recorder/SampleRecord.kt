package org.sagebionetworks.assessmentmodel.passivedata.recorder

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


interface SampleRecord {

    /// An identifier marking the current step.
    ///
    /// This is a path marker where the path components are separated by a '/' character. This path
    /// includes the task identifier and any sections or subtasks for the full path to the current
    /// step.
    /// TODO: implement step path - liujoshua 2021-09-14
    /// var stepPath: String

    /// The date timestamp when the measurement was taken (if available). This should be included
    /// for the first entry to mark the start of the recording. Other than to mark step changes,
    /// the `timestampDate` is optional and should only be included if required by the research
    /// study.
    val timestampDate: Instant?

    /// A timestamp that is relative to the system uptime.
    ///
    /// This should be included for the first entry to mark the start of the recording. Other than
    /// to mark step changes, the `timestamp` is optional and should only be included if required
    /// by the research study.
    ///
    /// On Apple devices, this is the timestamp used to mark sensors that run in the foreground
    /// only such as video processing and motion sensors.
    ///
    /// syoung 04/24/2019 Per request from Sage Bionetworks' research scientists, this timestamp is
    /// "zeroed" to when the recorder is started. It should be calculated by offsetting the
    /// `ProcessInfo.processInfo.systemUptime` from the monotonic clock time to account for gaps in
    /// the sampling due to the application becoming inactive. For example, if the participant
    /// accepts a phone call while the recorder is running.
    ///
    /// -seealso: `ProcessInfo.processInfo.systemUptime`
    val timestamp: Long?
}