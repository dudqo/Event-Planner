package com.example.eventplanner.screens.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventplanner.screens.home.MapViewModel

@ExperimentalMaterial3Api
@Composable
fun EventViewScreen(
    viewModel: MapViewModel = viewModel()
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
