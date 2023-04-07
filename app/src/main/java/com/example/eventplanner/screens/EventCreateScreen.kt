package com.example.eventplanner.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventplanner.domain.model.Event

@ExperimentalMaterial3Api
@Composable
fun EventCreateScreen() {

    var title = remember { mutableStateOf("") }
    var lati= remember { mutableStateOf(0.00) }
    var longi = remember { mutableStateOf(0.00) }
    var address = remember { mutableStateOf("") }
    var desc = remember { mutableStateOf("") }
    var time = remember { mutableStateOf("") }
    var isPrivate = remember { mutableStateOf(false) }
    var useCurrLocation = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = "Discard"
                        )

                    }
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = "Save"
                        )

                    }
                }

            )

        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            Row() {
                TextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text(text = "Event Title") }
                )

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = if (isPrivate.value) "Private" else "Public")
                    Switch(
                        checked = isPrivate.value,
                        onCheckedChange = { isPrivate.value = it }
                    )
                }
            }

            Row() {
                Text(
                    text = "Use current location",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Checkbox(
                    checked = useCurrLocation.value,
                    onCheckedChange = { useCurrLocation.value = it }
                )
            }

            TextField(
                value = address.value,
                onValueChange = { address.value = it },
                label = { Text(text = "Address") },
                enabled = useCurrLocation.value.not()
            )
            
            DatePicker(
                state = datePickerState
            )


        }
    }

}