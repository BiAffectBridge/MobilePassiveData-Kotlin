package org.sagebionetworks.assessmentmodel.passivedata

import kotlinx.datetime.Instant

interface ResultData {
    val identifier: String
    val startDate: Instant
    val endDate: Instant
}