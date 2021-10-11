package org.sagebionetworks.assessmentmodel.passivedata.recorder

import android.content.Context
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionConfiguration
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionStatus
import org.sagebionetworks.assessmentmodel.passivedata.recorder.motion.FlowRecorder
import java.io.File
import java.io.IOException
import java.io.PrintStream
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Base class for a Recorder that reads from a Flow and produces a FileResult in Json format.
 */
abstract class FlowJsonFileResultRecorder<in E>(
    override val identifier: String,
    override val configuration: AsyncActionConfiguration,
    override val scope: CoroutineScope,
    flow: Flow<E>,
    private val context: Context
) : FlowRecorder<E, FileResult>(
    identifier, configuration, scope, flow
) {
    override val result = CompletableDeferred<FileResult>()

    private val JSON_MIME_CONTENT_TYPE = "application/json"
    private val JSON_FILE_START = "["
    private val JSON_FILE_END = "]"
    private val JSON_OBJECT_DELIMINATOR = ","

    private lateinit var file: File
    protected lateinit var filePrintStream: PrintStream

    private val isFirstJsonObject = AtomicBoolean(true)

    override fun start() {
        file = getTaskOutputFile("$identifier.json")
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
        Napier.i("Completed handling flow")
        if (e == null || e is CancellationException) {
            filePrintStream.print(JSON_FILE_END)
            result.complete(
                FileResult(
                    identifier,
                    startTime ?: Clock.System.now(),
                    endTime ?: Clock.System.now(),
                    JSON_MIME_CONTENT_TYPE,
                    file.path
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