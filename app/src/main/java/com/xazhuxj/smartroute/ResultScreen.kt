package com.xazhuxj.smartroute

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun ResultScreen(onNavigateToCurveScreen: () -> Unit, modifier: Modifier = Modifier) {
//    Column {
//        Text("ResultScreen", modifier = Modifier.weight(1f))
//        Button(onClick = { onNavigateToCurveScreen() }) {
//            Text("Go to Friends List")
//        }
//    }
    val listSize = 100
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column {
        Row {
            Button(modifier = Modifier.weight(1f),
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(0)
                    }

                }
            ) {
                Text(text = "Scroll to the top")
            }

            Button(modifier = Modifier.weight(1f),
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(listSize - 1)
                    }
                }
            ) {
                Text(text = "Scroll to the end")
            }
        }

        LazyColumn(state = scrollState) {
            items(listSize) {
                GPointListItem(index = it)
            }
        }
    }
}


@Composable
fun GPointListItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Item #$index", style = MaterialTheme.typography.titleMedium)
    }
}