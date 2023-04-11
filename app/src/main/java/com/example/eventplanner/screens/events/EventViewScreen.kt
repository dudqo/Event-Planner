package com.example.eventplanner.screens.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventplanner.screens.home.MapViewModel

@ExperimentalMaterial3Api
@Composable
fun EventViewScreen(
    viewModel: MapViewModel = viewModel()
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetShadowElevation = 20.dp,
        sheetPeekHeight = 85.dp,
        sheetContent = {
            Column() {
                Text("Chat")
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
        ) {
            Button(onClick = {


            }) {

            }
        }
    }
}
