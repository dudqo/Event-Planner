package com.example.eventplanner.screens.events

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.eventplanner.MapEvent
import com.example.eventplanner.domain.model.Event
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.libraries.places.api.Places
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalMaterial3Api
@Composable
fun EventCreateScreen(
    navController: NavHostController,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = true)
    }
    val cameraPositionState = rememberCameraPositionState()
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val openDateDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = remember {derivedStateOf { datePickerState.selectedDateMillis != null }}
    val openDiscardDialog = remember { mutableStateOf(false) }
    val openMapDialog = remember { mutableStateOf(false) }
    val timeState = rememberTimePickerState()
    viewModel.placesClient = Places.createClient(LocalContext.current)

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
                        viewModel.onEvent(
                            EventsEvent.OnCreateEventClick
                        )
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
                    value = viewModel.title,
                    onValueChange = { viewModel.onEvent(EventsEvent.OnTitleChange(it)) },
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
                    checked = viewModel.useCurrLocation,
                    onCheckedChange = { viewModel.onEvent(EventsEvent.OnUseCurrLocationChange(it)) }
                )
            }
            Button(
                onClick = {
                    openMapDialog.value = true
                }
            ) {
                Text("Select Location")
            }
/*            if (openMapDialog.value) {
                Dialog(onDismissRequest = { openMapDialog.value = false }) {
                    GoogleMap(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize(),
                        uiSettings = uiSettings,
                        cameraPositionState = cameraPositionState,
                        onMapLongClick = {
                        }

                    ) {
                    }
                }

            }*/
                OutlinedTextField(
                    value = viewModel.address,
                    onValueChange = {
                        viewModel.onEvent(EventsEvent.OnAddressChange(it))
                        viewModel.searchPlaces(it)
                    },
                    label = { Text(text = "Address") },
                    enabled = viewModel.useCurrLocation.not()
                )
                Column() {
                    AnimatedVisibility(
                        viewModel.locationAutofill.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(viewModel.locationAutofill) {
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .clickable {
                                        viewModel.address = it.address
                                        viewModel.locationAutofill.clear()
                                        viewModel.getCoordinates(it)
                                    }) {
                                    Text(it.address)
                                }
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }

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
                    value = viewModel.desc,
                    onValueChange = { viewModel.onEvent(EventsEvent.OnDescChange(it)) },
                    label = { Text(text = "Event Description") },
                    minLines = 5
                )


            }
        }

}