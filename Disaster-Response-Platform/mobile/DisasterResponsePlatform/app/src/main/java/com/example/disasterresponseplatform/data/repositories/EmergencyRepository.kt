package com.example.disasterresponseplatform.data.repositories


import com.example.disasterresponseplatform.data.database.emergency.Emergency
import com.example.disasterresponseplatform.data.database.emergency.EmergencyDao
import javax.inject.Inject

class EmergencyRepository @Inject constructor(private val emergencyDao: EmergencyDao) {

    suspend fun insertEmergency(emergency: Emergency){
        emergencyDao.insertEmergency(emergency)
    }

    fun getAllEmergencies(): List<Emergency>? = emergencyDao.getAllEmergencies()

    suspend fun deleteAllEmergencies(){
        emergencyDao.deleteAllEmergencies()
    }

    suspend fun deleteEmergency(id: Int){
        emergencyDao.deleteEmergency(id)
    }
}