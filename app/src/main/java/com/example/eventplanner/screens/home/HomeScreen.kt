package com.example.eventplanner.screens.home

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.eventplanner.MapEvent
import com.example.eventplanner.graphs.EventScreen
import com.example.eventplanner.graphs.Graph
import com.example.eventplanner.screens.events.EventsScreen
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
    val scaffoldState = rememberBottomSheetScaffoldState()
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = true)
    }
    val cameraPositionState = rememberCameraPositionState()
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    viewModel.state.properties = MapProperties(isMyLocationEnabled = locationPermissionState.status.isGranted)

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetShadowElevation = 20.dp,
        sheetPeekHeight = 85.dp,

        sheetContent = {
            Column() {
                Text(
                    "Events Nearby...",
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
                LazyRow() {
                    items(100) {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                                .size(width = 140.dp, height = 200.dp)
                                .fillMaxWidth(),
                        ) {
                            Text(
                                text = "Event $it",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp)
                            )

                        }
                    }
                }
            }
        }
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