package com.example.eventplanner.screens.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventplanner.graphs.Graph
import com.example.eventplanner.screens.home.MapViewModel

@ExperimentalMaterial3Api
@Composable
fun EventViewScreen(
    viewModel: EventsViewModel = hiltViewModel()
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val openDeleteDialog = remember { mutableStateOf(false) }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetShadowElevation = 20.dp,
        sheetPeekHeight = 85.dp,
        sheetContent = {
            Column() {
                Text(
                    text = "Chat",
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
            }
        }
    ) {
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
                            navController.navigate()
                        }) {
                            Text(
                                text = "Edit"
                            )

                        }
                        TextButton(onClick = {
                            openDeleteDialog.value = true
                        }) {
                            Text(
                                text = "Delete"
                            )

                        }
                    }

                )

            }
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = event.title,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = event.time,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = event.address,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = event.desc,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }

        }
    }
}
