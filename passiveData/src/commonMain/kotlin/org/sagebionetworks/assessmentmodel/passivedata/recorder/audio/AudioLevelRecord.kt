package org.sagebionetworks.assessmentmodel.passivedata.recorder.audio

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.sagebionetworks.assessmentmodel.passivedata.recorder.SampleRecord

/**
 * @param timestampDate The date timestamp when the measurement was taken (if available)
 * @param timestamp Time that the system has been awake since last reboot
 * @param uptime System clock time
 * @param timeInterval The sampling time interval
 * @param average The average meter level over the time interval
 * @param peak The peak meter level for the time interval
 * @param unit The unit of measurement for the decibel levels
 */
@Serializable
data class AudioLevelRecord(
    override val timestampDate: Instant?,
    override val timestamp: Long?,
    val uptime: Long?,
    val timeInterval: Long?,
    val average: Float?,
    val peak: Float?,
    val unit: String?
) :
    SampleRecord {
}