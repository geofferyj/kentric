package me.geoffery.kentric.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.job
import me.geoffery.kentric.ui.GeneratorScreenViewModel

@Composable
fun EntryItem(index: Int, viewModel: GeneratorScreenViewModel) {
    val entries by viewModel.entryStates
    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(index) {
        if (index == entries.size - 1) {
            this.coroutineContext.job.invokeOnCompletion {
                focusRequester.requestFocus()
            }        }

    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start=16.dp, bottom = 16.dp),
    ) {

        Text(text = "${index + 1}")
        TextField(
            value = entries[index],
            onValueChange = {
                viewModel.updateEntry(index, it)
            },
            singleLine = true,
            modifier = Modifier
                .padding(start=16.dp)
                .focusRequester(focusRequester)
                ,
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            )
        )
    }
}