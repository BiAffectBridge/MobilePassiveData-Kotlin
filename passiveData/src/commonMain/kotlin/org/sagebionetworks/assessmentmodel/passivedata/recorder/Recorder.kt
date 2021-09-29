package org.sagebionetworks.assessmentmodel.passivedata.recorder

import org.sagebionetworks.assessmentmodel.passivedata.ResultData
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionController

interface Recorder<R : ResultData> : AsyncActionController<R> {
}