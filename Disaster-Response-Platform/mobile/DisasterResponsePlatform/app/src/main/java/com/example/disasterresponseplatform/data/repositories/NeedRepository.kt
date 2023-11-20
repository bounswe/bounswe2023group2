package com.example.disasterresponseplatform.data.repositories

import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.data.database.need.NeedDao
import javax.inject.Inject

class NeedRepository @Inject constructor(private val needDao: NeedDao) {

    suspend fun insertNeed(need: Need){
        needDao.insertNeed(need)
    }

    fun getX(creatorID: String): Double?{
        return needDao.getX(creatorID)
    }

    fun getY(creatorID: String): Double?{
        return needDao.getY(creatorID)
    }

    fun getAllNeeds(): List<Need>? = needDao.getAllNeeds()

    suspend fun updateNeed(need: Need) {
        needDao.updateNeed(need)
    }

}