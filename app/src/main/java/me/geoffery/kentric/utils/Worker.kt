package me.geoffery.kentric.utils

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.text.TextPaint
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import me.geoffery.kentric.combinatoricskt.permutationsWithRepetition
import me.geoffery.kentric.ui.FileType
import me.geoffery.kentric.utils.MediaStoreUtils.scanPath
import me.geoffery.kentric.utils.MediaStoreUtils.scanUri
import org.koin.core.component.KoinComponent
import java.io.File
import java.io.IOException

class Worker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params),
    KoinComponent {

    private val context: Context
        get() = applicationContext

    override suspend fun doWork(): Result {

        val noOfWays = inputData.getInt("noOfWays", 0)
        val entries = inputData.getStringArray("entries") ?: return Result.failure()

        generatePdfDocument(noOfWays, entries)

        return Result.success()
    }

    private suspend fun createPdf(document: PdfDocument, type: FileType = FileType.Pdf) {
        val filename = "kentric codes.${type.extension}"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val uri = MediaStoreUtils.createDocumentUri(context, filename)
                ?: return
            try {
                context.contentResolver.openOutputStream(uri, "w")?.use { outputStream ->
                    document.writeTo(outputStream)
                    scanUri(context, uri, type.mimeType)

                    Log.d("Worker", "Pdf created")
                }

            } catch (e: IOException) {
                Log.e("Worker", "Error creating pdf", e)
            }
        } else {
            val documentsFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

            val file = File(documentsFolder, filename)

            try {
                file.outputStream().use { outputStream ->
                    document.writeTo(outputStream)
                    scanPath(context, file.absolutePath, type.mimeType)

                }
            } catch (e: IOException) {

            }
        }

    }

    private suspend fun generatePdfDocument(noOfWays: Int, entries: Array<String>) {
        val permutations = entries.permutationsWithRepetition(noOfWays)
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        var page = pdfDocument.startPage(pageInfo)
        val noOfPages = permutations.totalSize.toInt() / 40
        Log.d("Worker", "No of pages: $noOfPages")
        Log.d("Worker", "totalSize: ${permutations.totalSize}")
        var lineInPage = 1
        val totalLinesPerPage = 40
        permutations.forEachIndexed{ index, entry ->
            if (index > 0 && (index % totalLinesPerPage == 0)) {
                pdfDocument.finishPage(page)
                page = pdfDocument.startPage(pageInfo)
                lineInPage = 1
            }
            val line = "${index + 1}. ${entry.joinToString(",") { it }}"
            val canvas = page.canvas
            val paint = TextPaint()
            paint.textSize = 16f
//            paint.color = Color.RED
            canvas.drawText(line, 20f, (lineInPage) * 20f, paint)
            lineInPage++
        }
        pdfDocument.finishPage(page)
        createPdf(pdfDocument)
        pdfDocument.close()
    }


//    override suspend fun getForegroundInfo(): ForegroundInfo {
//        // Todo: show notification
//        val notificationService: NotificationService by inject()
//        val notification = notificationService.createNotification(
//            "PeniWallet",
//            "Executing transaction",
//        )
//        return ForegroundInfo(1, notification)
//    }
}