package com.example.disasterresponseplatform.data.repositories

import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.data.database.need.NeedDao
import javax.inject.Inject

class NeedRepository @Inject constructor(private val needDao: NeedDao) {

    suspend fun insertNeed(need: Need){
        needDao.insertActivation(need)
    }

    fun getLocation(creatorID: String): String{
        return needDao.getLocation(creatorID)
    }

}