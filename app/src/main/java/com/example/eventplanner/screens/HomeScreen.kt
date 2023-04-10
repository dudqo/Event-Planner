package com.example.eventplanner.screens

import android.Manifest
import android.annotation.SuppressLint
import android.widget.SearchView
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eventplanner.MapEvent
import com.example.eventplanner.graphs.Graph
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*



@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    viewModel: MapViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = true)
    }
    val cameraPositionState = rememberCameraPositionState()
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    viewModel.state.properties = MapProperties(isMyLocationEnabled = locationPermissionState.status.isGranted)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        GoogleMap(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            properties = viewModel.state.properties,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState,
            onMapLongClick = {
                viewModel.onEvent(
                    MapEvent.OnMapLongClick(it)
                )
            }

        ) {
            if (viewModel.longPressed) {
                Marker(
                    position = LatLng(viewModel.lat, viewModel.lng),
                    title = "CREATE NEW EVENT",
                    snippet = "Tap to create new event",
                    onInfoWindowClick = {
                        //navController.navigate(Graph.EVENT)

                    }

                )
            }


            viewModel.state.eventLocation.forEach {locat ->
                Marker(
                    position = LatLng(locat.lat, locat.lng),
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