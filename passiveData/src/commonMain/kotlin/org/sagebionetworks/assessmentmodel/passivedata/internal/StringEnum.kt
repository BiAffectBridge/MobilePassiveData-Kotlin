package org.sagebionetworks.assessmentmodel.passivedata.internal

/**
 * A string enum is an enum that uses a string as its raw value.
 */
interface StringEnum {
    val name: String
    val serialName: String?
        get() = null
}