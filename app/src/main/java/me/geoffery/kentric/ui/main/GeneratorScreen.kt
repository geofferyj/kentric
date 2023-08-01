package me.geoffery.kentric.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import me.geoffery.kentric.ui.main.destinations.ResultsScreenDestination
import me.geoffery.kentric.ui.theme.KentricTheme


@Destination(start = true)
@Composable
fun GeneratorScreen(
    viewModel: GeneratorScreenViewModel,
    navigator: DestinationsNavigator
) {

    val noOfWays by viewModel.noOfWaysState
    val entries by viewModel.entryStates

    val lazyColumnState = rememberLazyListState()

    LaunchedEffect(entries.size) {
        lazyColumnState.animateScrollToItem(entries.size)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Enter Value",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                ,
            state = lazyColumnState,

        ) {
            items(entries.size) { index ->
                EntryItem(index, viewModel)
            }
        }

        TextField(
            value = noOfWays.toString(),
            onValueChange = {
                try {
                    if (it.isEmpty()) {
                        viewModel.noOfWaysState.value = 0
                        return@TextField
                    }
                    viewModel.noOfWaysState.value = it.toInt()
                } catch (_: Exception) {

                }
            },
            singleLine = true,
            label = { Text(text = "Enter Number of Ways") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.5f),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(5.dp),

        )

        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { viewModel.addEntry() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                modifier = Modifier.width(100.dp)
            ) {
                Text(text = "Add")
            }

            Button(
                onClick = { viewModel.removeEntry() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.width(100.dp)

            ) {
                Text(text = "Remove")
            }

        }
        Button(onClick = {
            navigator.navigate(ResultsScreenDestination)
            viewModel.generate()
        }) {
            Text(text = "Generate")
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GeneratorScreenPreview() {
    KentricTheme {
        GeneratorScreen(
            GeneratorScreenViewModel(),
            EmptyDestinationsNavigator
        )
    }
}