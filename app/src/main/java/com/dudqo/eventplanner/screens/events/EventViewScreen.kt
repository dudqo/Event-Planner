package com.dudqo.eventplanner.screens.events

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.dudqo.eventplanner.graphs.EventScreen


@ExperimentalMaterial3Api
@Composable
fun EventViewScreen(
    navController: NavHostController,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val openDeleteDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

/*    BottomSheetScaffold(
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
        }*/
    Surface(
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
                            navController.navigate(EventScreen.CreateScreen.route
                            + "?eventId=${viewModel.currentEventId}") {
                                popUpTo("events")
                            }
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
            if (openDeleteDialog.value) {
                AlertDialog(
                    onDismissRequest = { openDeleteDialog.value = false },
                    title = {
                        Text(text = "Delete this event?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                openDeleteDialog.value = false
                                navController.popBackStack()
                                viewModel.deleteImages(viewModel.selectedImages)
/*                                if (viewModel.deleted) {
                                    Toast.makeText(context, "file deleted", Toast.LENGTH_SHORT).show()
                                }*/
                                viewModel.onEvent(EventsEvent.OnDeleteEventClick(viewModel.currEvent))
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openDeleteDialog.value = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }

                )
            }

            Column(
                modifier = Modifier.padding(it).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                if (viewModel.selectedImages.isNotEmpty()) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        items(viewModel.selectedImages) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
                                    .padding(horizontal = 8.dp, vertical = 8.dp)
                            ) {
                                AsyncImage(model = Uri.parse(it), contentDescription = null)
                            }
                        }
                    }
                }
                Text(
                    text = viewModel.title,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    fontSize = 50.sp
                )
                Spacer(Modifier.height(15.dp))
                Divider()
                Text(
                    text = "Time",
                    textAlign = TextAlign.Left,
                    fontSize = 15.sp
                )
                Spacer(Modifier.height(15.dp))
                Text(
                    text = viewModel.time,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
                Spacer(Modifier.height(15.dp))
                Divider()
                Text(
                    text = "Location",
                    textAlign = TextAlign.Left,
                    fontSize = 15.sp
                )
                Spacer(Modifier.height(15.dp))
                Text(
                    text = viewModel.address,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(15.dp))
                Divider()
                Text(
                    text = "Description",
                    textAlign = TextAlign.Left,
                    fontSize = 15.sp
                )
                Spacer(Modifier.height(15.dp))
                Text(
                    text = viewModel.desc,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

        }
    }
}
