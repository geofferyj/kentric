package me.geoffery.kentric.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import me.geoffery.kentric.utils.forEachIndexedCaped

@Destination
@Composable
fun ResultsScreen(
    viewModel: GeneratorScreenViewModel
) {

    val permutations by viewModel.results

    Column(
        modifier= Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(16.dp),
        ) {
            permutations?.let { entries->

                entries.forEachIndexedCaped(10_000){ index, entry ->
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(text = "${index+1}.")
                            Text(text = entry.joinToString(",") { it  })
                        }
                    }

                }


            }

        }

        Button(
            onClick = {
                      viewModel.generatePdf()
            },
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),

        ) {
            Text(text = "Generate PDF")
        }
    }


}