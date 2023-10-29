package com.example.disasterresponseplatform.data.repositories

import com.example.disasterresponseplatform.data.database.event.Event
import com.example.disasterresponseplatform.data.database.event.EventDao
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventDoa: EventDao) {

    suspend fun insertEvent(event: Event){
        eventDoa.insertActivation(event)
    }

    fun getLocation(creatorID: String): String{
        return eventDoa.getLocation(creatorID)
    }

}