package com.example.disasterresponseplatform.data.repositories

import androidx.room.Query
import com.example.disasterresponseplatform.data.database.DatabaseInfo
import com.example.disasterresponseplatform.data.database.event.Event
import com.example.disasterresponseplatform.data.database.event.EventCols
import com.example.disasterresponseplatform.data.database.event.EventDao
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventDao: EventDao) {

    suspend fun insertEvent(event: Event){
        eventDao.insertEvent(event)
    }

    fun getAllEvents(): List<Event>?{
        return eventDao.getAllEvents()
    }

    suspend fun deleteEvent(id: Int){
        eventDao.deleteEvent(id)
    }

}