package com.example.disasterresponseplatform.ui.activity.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterresponseplatform.data.database.event.Event
import com.example.disasterresponseplatform.data.repositories.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel@Inject constructor(private val eventRepository: EventRepository) : ViewModel() {

    /**
     * It inserts event in a background (IO) thread
     */
    fun insertEvent(event: Event){
        viewModelScope.launch(Dispatchers.IO){
            eventRepository.insertEvent(event)
        }
    }

    fun getLocation(creatorID: String): String{
        return eventRepository.getLocation(creatorID)
    }

}