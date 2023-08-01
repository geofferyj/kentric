package me.geoffery.kentric.ui.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.github.shiguruikai.combinatoricskt.CombinatorialSequence
import com.github.shiguruikai.combinatoricskt.permutationsWithRepetition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GeneratorScreenViewModel : ViewModel(), KoinComponent {

    val noOfWaysState: MutableState<Int> = mutableIntStateOf(0)
    val entryStates: MutableState<List<String>> = mutableStateOf(listOf(""))
    val results = mutableStateOf<CombinatorialSequence<List<String>>?>(null)

    init {

        resetResults()
    }


    fun addEntry() {
        val tempList = entryStates.value.toMutableList()
        tempList.add("")
        entryStates.value = tempList
    }

    fun removeEntry() = try {
        val tempList = entryStates.value.toMutableList()

        tempList.removeLast()
        entryStates.value = tempList

    } catch (e: Exception) {
        // do nothing
    }

    fun updateEntry(index: Int, value: String) {
        val tempList = entryStates.value.toMutableList()
        tempList[index] = value.uppercase()
        entryStates.value = tempList
    }

    fun generate() {
        val entries = entryStates.value
        val noOfWays = noOfWaysState.value
        viewModelScope.launch(Dispatchers.IO) {
            val permutations = entries.permutationsWithRepetition(noOfWays)

            results.value = permutations
            println("done")

        }
    }

    fun generatePdf() {
        val workManager: WorkManager by inject()
        val entries = entryStates.value
        val noOfWays = noOfWaysState.value
        viewModelScope.launch(Dispatchers.IO) {
            val newUniqueWork = OneTimeWorkRequestBuilder<Worker>()
                .setInputData(
                    Data.Builder()
                        .putInt("noOfWays", noOfWays)
                        .putStringArray("entries", entries.toTypedArray())
                        .build()
                )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()

            workManager.enqueueUniqueWork("generate_pdf", ExistingWorkPolicy.KEEP, newUniqueWork)

        }
    }

    private fun resetResults() {
        results.value = null
    }


}

sealed class FileType(val extension: String, val mimeType: String) {
    object Pdf : FileType("pdf", "application/pdf")
    object Zip : FileType("zip", "application/zip")
}