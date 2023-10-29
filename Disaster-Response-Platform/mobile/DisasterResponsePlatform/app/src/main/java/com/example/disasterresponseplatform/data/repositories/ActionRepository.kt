package com.example.disasterresponseplatform.data.repositories

import com.example.disasterresponseplatform.data.database.action.Action
import com.example.disasterresponseplatform.data.database.action.ActionDao
import javax.inject.Inject

class ActionRepository @Inject constructor(private val actionDao: ActionDao) {

    suspend fun insertAction(action: Action){
        actionDao.insertActivation(action)
    }

    fun getStartLocation(creatorID: String): String{
        return actionDao.getStartLocation(creatorID)
    }

}