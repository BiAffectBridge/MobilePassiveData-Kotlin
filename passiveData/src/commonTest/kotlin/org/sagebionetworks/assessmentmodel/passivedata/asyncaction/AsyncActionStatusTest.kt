package org.sagebionetworks.assessmentmodel.passivedata.asyncaction

import kotlin.test.Test
import kotlin.test.assertTrue
import  org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionStatus.*
import kotlin.test.assertEquals


class AsyncActionStatusTest {

    @Test
    fun testCompare() {
        assertTrue(RUNNING > STARTING)
        assertTrue(RUNNING < FINISHED)
    }

    @Test
    fun testDescription() {
        assertEquals("requestingPermission", REQUESTING_PERMISSION.description)
    }
}