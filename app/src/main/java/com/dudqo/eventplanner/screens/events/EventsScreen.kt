package com.dudqo.eventplanner.screens.events

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dudqo.eventplanner.graphs.EventScreen
import com.dudqo.eventplanner.graphs.Graph

@ExperimentalMaterial3Api
@Composable
fun EventsScreen(
    navController: NavController,
    viewModel: EventsViewModel = hiltViewModel()
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Graph.EVENT)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Create New Event")
            }
        }
    ) {
        Column() {
            Text(
                "My Events", textAlign = TextAlign.Left, fontWeight = FontWeight.Bold, fontSize = 30.sp
            )
            LazyColumn(
                modifier = Modifier.padding(it),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center,
            ) {
                items(viewModel.state.events) {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    EventScreen.ViewScreen.route +
                                            "?eventId=${it.id}"
                                ) },
                    ) {
                        Text(
                            text = it.title,
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

}
