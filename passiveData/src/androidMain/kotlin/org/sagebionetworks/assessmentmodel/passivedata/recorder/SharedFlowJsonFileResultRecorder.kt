package org.sagebionetworks.assessmentmodel.passivedata.recorder

import android.content.Context
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionConfiguration
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionStatus
import org.sagebionetworks.assessmentmodel.passivedata.recorder.motion.SharedFlowRecorder
import java.io.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

abstract class SharedFlowJsonFileResultRecorder<in E>(
    override val identifier: String,
    override val configuration: AsyncActionConfiguration,
    override val scope: CoroutineScope,
    flow: SharedFlow<E>,
    private val filename: String,
    private val context: Context
) : SharedFlowRecorder<E, FileResult>(
    identifier, configuration, scope, flow
) {
    override val result = CompletableDeferred<FileResult>()

    private val JSON_MIME_CONTENT_TYPE = "application/json"
    private val JSON_FILE_START = "["
    private val JSON_FILE_END = "]"
    private val JSON_OBJECT_DELIMINATOR = ","

    private lateinit var file: File
    protected lateinit var filePrintStream: PrintStream
    private lateinit var startDate: Instant

    private val isFirstJsonObject = AtomicBoolean(true)

    override fun start() {
        startDate = Clock.System.now()
        file = getTaskOutputFile(filename)
        filePrintStream = PrintStream(file)
        filePrintStream.print(JSON_FILE_START)
        super.start()
    }

    @Throws(IOException::class)
    open fun getTaskOutputFile(
        filename: String
    ): File {
        val path = context.filesDir
        val outputFilename = "${UUID.randomUUID()}/$filename"
        val outputFile = File(path, outputFilename)
        if (!outputFile.isFile && !outputFile.exists()) {
            outputFile.parentFile!!.mkdirs()
            outputFile.createNewFile()
        }
        return outputFile
    }

    override suspend fun handleElement(e: E) {
        if (!isFirstJsonObject.compareAndSet(true, false)) {
            filePrintStream.print(JSON_OBJECT_DELIMINATOR)
        }
        serializeElement(e)
    }

    abstract fun serializeElement(e: E)

    override fun completedHandlingFlow(e: Throwable?) {

        if (e == null) {
            filePrintStream.print(JSON_FILE_END)
            result.complete(
                FileResult(
                    filename,
                    startDate,
                    Clock.System.now(),
                    JSON_MIME_CONTENT_TYPE,
                    filename
                )
            )
            _asyncStatus = AsyncActionStatus.FINISHED
            filePrintStream.close()
        } else {
            result.completeExceptionally(e)
            filePrintStream.close()
            file.delete()
        }
    }

}