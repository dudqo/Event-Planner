package com.example.eventplanner.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.eventplanner.domain.model.Event
import java.util.*

@ExperimentalMaterial3Api
@Composable
fun EventCreateScreen(
    navController: NavHostController,
    viewModel: EventsViewModel = viewModel()
) {

    var title = remember { mutableStateOf("") }
    var lati= remember { mutableStateOf(0.00) }
    var longi = remember { mutableStateOf(0.00) }
    var address = remember { mutableStateOf("") }
    var desc = remember { mutableStateOf("") }
    var time = remember { mutableStateOf("") }
    var isPrivate = remember { mutableStateOf(false) }
    var useCurrLocation = remember { mutableStateOf(false) }
    val openDateDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = remember {derivedStateOf { datePickerState.selectedDateMillis != null }}
    val openDiscardDialog = remember { mutableStateOf(false) }
    val timeState = rememberTimePickerState()

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
                    TextButton(onClick = {
                        openDiscardDialog.value = true
                    }) {
                        Text(
                            text = "Discard"
                        )

                    }
                    TextButton(onClick = {
                        viewModel.saveEvent()
                        navController.popBackStack()
                    }) {
                        Text(
                            text = "Create"
                        )

                    }
                }

            )

        }
    ) {
        if (openDiscardDialog.value) {
            AlertDialog(
                onDismissRequest = { openDiscardDialog.value = false },
                title = {
                    Text(text = "Discard Changes?")
                },
                text = {
                    Text(
                        "All unsaved changes will be lost"
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDiscardDialog.value = false
                            navController.popBackStack()
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDiscardDialog.value = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }

            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row() {
                TextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text(text = "Event Title") }
                )

/*                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = if (isPrivate.value) "Private" else "Public")
                    Switch(
                        checked = isPrivate.value,
                        onCheckedChange = { isPrivate.value = it }
                    )
                }*/
            }

            Spacer(Modifier.height(30.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
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

            Spacer(Modifier.height(30.dp))

            Row() {
                Button(
                    onClick = {
                        openDateDialog.value = true
                    }
                ) {
                    Text("Select Date")
                }
/*                TextButton(
                    onClick = {
                        openTimeDialog.value = true
                    }
                ) {
                    Text("Select Time")
                }*/
            }
            if (openDateDialog.value) {

                DatePickerDialog(
                    onDismissRequest = {
                        openDateDialog.value = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                openDateDialog.value = false
                            },
                            enabled = confirmEnabled.value
                        ) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openDateDialog.value = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }


            }
            Text("Date: ${datePickerState.selectedDateMillis}")

            TimeInput(state = timeState)

            Spacer(Modifier.height(30.dp))

            TextField(
                value = desc.value,
                onValueChange = { desc.value = it },
                label = { Text(text = "Event Description") },
                minLines = 5
            )


        }
    }

}