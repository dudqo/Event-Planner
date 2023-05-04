package com.dudqo.eventplanner.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dudqo.eventplanner.screens.friends.FriendsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun FriendsScreen(
    viewModel: FriendsViewModel = viewModel()
    /*    userData: UserData?,
        onSignOut: () -> Unit*/
) {
    /*Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row() {
            if (userData?.profilePictureUrl != null) {
                AsyncImage(
                    model = userData.profilePictureUrl,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Column() {
                Text(text = userData.userName!!)
                Text(text = userData.userEmail)
            }
        }
    }*/
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
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Friend"
                        )
                    }
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Person"
                        )
                    }
                }

            )

        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it),
            verticalArrangement = Arrangement.Center
        ) {
           /* Divider()
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.clickable { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = viewModel.profilePic,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = viewModel.userEmail!!)
                    Spacer(Modifier.height(15.dp))
                    Text(text = viewModel.userName!!)
                }

            }
            Spacer(Modifier.height(10.dp))
            Divider()*/
            Text(
                "Friends:",
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                //contentAlignment = Alignment.Center
            ) {
                items(100) {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = "Friend $it",
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