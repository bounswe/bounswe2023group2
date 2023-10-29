package com.example.disasterresponseplatform.data.repositories

import com.example.disasterresponseplatform.data.database.userdata.UserData
import com.example.disasterresponseplatform.data.database.userdata.UserDataDao
import javax.inject.Inject


class UserDataRepository @Inject constructor(private val userDataDao: UserDataDao) {

    suspend fun insertUserData(userData: UserData){
        userDataDao.insertActivation(userData)
    }

    fun getEmail(username: String): String{
        return userDataDao.getEmail(username)
    }
}