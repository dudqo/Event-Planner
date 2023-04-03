package com.example.eventplanner.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventplanner.domain.model.Event

@ExperimentalMaterial3Api
@Composable
fun EventCreateScreen(
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