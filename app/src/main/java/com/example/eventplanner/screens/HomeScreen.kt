package com.example.eventplanner.screens

import android.annotation.SuppressLint
import android.widget.SearchView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventplanner.MapEvent
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker


@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    viewModel: MapViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false)
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        GoogleMap(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            properties = viewModel.state.properties,
            uiSettings = uiSettings,
            onMapLongClick = {
                viewModel.onEvent(
                    MapEvent.OnMapLongClick(it)
                )
            }

        ) {
            if (viewModel.longPressed) {
                Marker(
                    position = LatLng(viewModel.lati, viewModel.longi),
                    title = "CREATE NEW EVENT",
                    snippet = "Tap to create new event",
                    onInfoWindowClick = {
                        TODO()

                    }

                )
            }


            viewModel.state.eventLocation.forEach {locat ->
                Marker(
                    position = LatLng(locat.lati, locat.longi),
                    title = "Title of the event",
                    snippet = "Long click to create new event",
                    onInfoWindowLongClick = {
                        viewModel.onEvent(
                            MapEvent.onInfoWindowLongClick(locat)
                        )
                    }

                )
            }
        }
    }
}
