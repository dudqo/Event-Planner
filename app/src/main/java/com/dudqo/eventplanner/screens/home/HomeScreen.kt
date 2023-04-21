package com.dudqo.eventplanner.screens.home

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dudqo.eventplanner.MapEvent
import com.dudqo.eventplanner.graphs.EventScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class, DelicateCoroutinesApi::class)
@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: MapViewModel = hiltViewModel()
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = true)
    }
    val cameraPositionState = rememberCameraPositionState()
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    viewModel.state.properties = viewModel.state.properties.copy(
        isMyLocationEnabled = locationPermissionState.status.isGranted
    )
    viewModel.fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(LocalContext.current)

    LaunchedEffect(viewModel.state.lastKnownLocation) {
        if (locationPermissionState.status.isGranted) {
            viewModel.getDeviceLocation()
        }
        if (viewModel.state.lastKnownLocation != null) {
            cameraPositionState.animate(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(
                        LatLng(
                            viewModel.lat,
                            viewModel.lng
                        ), 13f
                    )
                )
            )
        }
    }

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
/*            if (viewModel.longPressed) {
                Marker(
                    position = LatLng(viewModel.lat, viewModel.lng),
                    title = "CREATE NEW EVENT",
                    snippet = "Tap to create new event",
                    onInfoWindowClick = {
                        //navController.navigate(Graph.EVENT)

                    }

                )
            }*/


            viewModel.state.events.forEach {
                if (!(it.lat == 0.00 && it.lng == 0.00 && it.address == "")) {
                    Marker(
                        position = LatLng(it.lat, it.lng),
                        title = it.title,
                        snippet = "Tap to view event details",
                        onInfoWindowClick = {marker ->
                            GlobalScope.launch(Dispatchers.Main) {
                                navController.navigate(
                                    EventScreen.ViewScreen.route +
                                            "?eventId=${it.id}"
                                )
                            }
                        }

                    )
                }
            }
        }
    }
}