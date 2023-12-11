package com.example.disasterresponseplatform.data.repositories

import com.example.disasterresponseplatform.data.database.action.Action
import com.example.disasterresponseplatform.data.database.action.ActionDao
import com.example.disasterresponseplatform.data.database.need.Need
import javax.inject.Inject

class ActionRepository @Inject constructor(private val actionDao: ActionDao) {

    suspend fun insertAction(action: Action){
        actionDao.insertAction(action)
    }

    fun getAllActions(): List<Action>? = actionDao.getAllActions()

    fun getStartLocation(creatorID: String): String{
        return actionDao.getStartLocation(creatorID)
    }

}