package com.example.eventplanner.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.eventplanner.domain.model.Event
import com.example.eventplanner.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: EventRepository
) {

    var title by mutableStateOf("")
    var lat by mutableStateOf(0.00)
    var lng by mutableStateOf(0.00)
    var address by mutableStateOf("")
    var desc by mutableStateOf("")
    var time by mutableStateOf("")
    var isPrivate by mutableStateOf(false)
    var useCurrLocation by mutableStateOf(false)


    fun onTitleChange(newTitle: String) {
        title = newTitle
    }

    fun onAddressChange(newAddress: String) {
        address = newAddress
    }

    fun onDescChange(newDesc: String) {
        desc = newDesc
    }

    fun onUseCurrLocationChange(newUseCurrLocation: Boolean) {
        useCurrLocation = newUseCurrLocation
    }




    suspend fun saveEvent(){
        repository.insertEvent(
            Event(title, lat, lng, address, desc, time, isPrivate)
        )
    }
}