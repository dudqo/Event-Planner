package com.dudqo.eventplanner.screens.events

import android.Manifest
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.text.method.TextKeyListener.clear
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Collections.addAll
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPermissionsApi::class, ExperimentalLayoutApi::class)
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
    val multipleStoragePermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

    val context = LocalContext.current
    val openDateDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = remember {derivedStateOf { datePickerState.selectedDateMillis != null }}
    val openDiscardDialog = remember { mutableStateOf(false) }
    val openTimeDialog = remember { mutableStateOf(false) }
    val openWarningDialog = remember { mutableStateOf(false) }
    val timeState = rememberTimePickerState()
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = {
            viewModel.uris = it
        })
    viewModel.placesClient = Places.createClient(context)
    viewModel.fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)
    viewModel.geoCoder = Geocoder(context)


    BackHandler {
        openWarningDialog.value = true
    }
    LaunchedEffect(Unit) {
        if (locationPermissionState.status.isGranted) {
            viewModel.getDeviceLocation()
        }
    }
    if (openWarningDialog.value) {
        AlertDialog(
            onDismissRequest = { openWarningDialog.value = false },
            title = {
                Text(text = "Exit without saving?")
            },
            text = {
                Text(
                    "All unsaved changes will be lost"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openWarningDialog.value = false
                        navController.popBackStack()
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openWarningDialog.value = false
                    }
                ) {
                    Text("Cancel")
                }
            }

        )
    }

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
                        openWarningDialog.value = true
                    }) {
                        Text(
                            text = "Discard"
                        )

                    }
                    TextButton(onClick = {
                        if (viewModel.title == "") {
                            Toast.makeText(context, "Title is required to create an event", Toast.LENGTH_SHORT).show()
                        } else {
                            val savedUris = mutableListOf<Uri>()
                            for (uri in viewModel.uris) {
                                val inputStream = context.contentResolver.openInputStream(uri)
                                val bitmap = BitmapFactory.decodeStream(inputStream)

                                val filename = "${System.currentTimeMillis()}.jpg"
                                context.openFileOutput(filename, MODE_PRIVATE).use {stream ->
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                                }


                                val file = File(context.filesDir, filename)
                                savedUris.add(Uri.fromFile(file))
                            }
                            viewModel.selectedImages = savedUris.map {uri ->
                                uri.toString()
                            }
                            if (viewModel.tempImages != viewModel.selectedImages) {
                                viewModel.deleteImages(viewModel.tempImages)
                            }
/*                            if (viewModel.deleted) {
                                Toast.makeText(context, "file deleted", Toast.LENGTH_SHORT).show()
                            }*/

                            viewModel.onEvent(
                                EventsEvent.OnCreateEventClick
                            )
                            navController.popBackStack()
                        }
                    }) {
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
                .verticalScroll(rememberScrollState())
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
                    text = if (locationPermissionState.status.isGranted)
                        "Use current location" else
                            "Use current location (not available)",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Checkbox(
                    checked = viewModel.useCurrLocation,
                    onCheckedChange = { viewModel.onEvent(EventsEvent.OnUseCurrLocationChange(it)) },
                    enabled = locationPermissionState.status.isGranted

                )
            }
/*            Button(
                onClick = {
                    openMapDialog.value = true
                }
            ) {
                Text("Select Location")
            }
            if (openMapDialog.value) {
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
                    visible = viewModel.locationAutofill.isNotEmpty() && !viewModel.useCurrLocation,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.height(200.dp),
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
                    Text("Select date and time")
                }
                Spacer(Modifier.width(10.dp))
                /*                    Button(
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
                                openTimeDialog.value = true
                                viewModel.timeInMillis = datePickerState.selectedDateMillis!! + TimeUnit.HOURS.toMillis(4)
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
                    DatePicker(
                        state = datePickerState
                    )
                }


            }

            if (openTimeDialog.value) {
                Dialog(
                    onDismissRequest = { openTimeDialog.value = false },
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TimePicker(state = timeState)
                        Button(
                            onClick = {
                                openTimeDialog.value = false
                            }
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
            //TimeInput(state = timeState)
            if (viewModel.timeInMillis == 0L) {
                Text("Date not selected")
            } else {
                if (timeState.hour == 0 && timeState.minute == 0) {
                    viewModel.time = SimpleDateFormat(
                        "EEEE, MMM d, yyyy",
                        Locale.US
                    ).format(viewModel.timeInMillis)
/*                    if (timeState.hour > 11) {
                        viewModel.time += " ${timeState.hour - 11}:${timeState.minute} PM"
                    } else if (timeState.hour in 1..11) {
                        viewModel.time += " ${timeState.hour}:${timeState.minute} AM"
                    } else {
                        viewModel.time += " 12:${timeState.minute} AM"
                    }*/
                } else {
/*                    viewModel.timeInMillis += TimeUnit.HOURS.toMillis((timeState.hour).toLong()) +
                            TimeUnit.MINUTES.toMillis(timeState.minute.toLong())*/
                    viewModel.time = SimpleDateFormat(
                        "EEEE, MMM d, yyyy hh:mm aaa",
                        Locale.US
                    ).format(viewModel.timeInMillis + TimeUnit.HOURS.toMillis((timeState.hour).toLong()) +
                            TimeUnit.MINUTES.toMillis(timeState.minute.toLong()))
                }
                Text("Date: ${viewModel.time}")
            }

            Spacer(Modifier.height(30.dp))


            Button(
                onClick = {
                    //viewModel.deleteImages()
                    if (multipleStoragePermissionsState.allPermissionsGranted) {
                        photoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    } else {
                        multipleStoragePermissionsState.launchMultiplePermissionRequest()
                    }
                }
            ) {
                Text("Choose Photos")
            }
            LazyRow(
                modifier = Modifier.height(200.dp),
                horizontalArrangement =  Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.uris) {
                    AsyncImage(model = it, contentDescription = null)
                }
            }
            Column() {
                for (uri in viewModel.selectedImages) {
                    Text(uri.toString())
                }
            }

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


