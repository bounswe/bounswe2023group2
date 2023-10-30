package com.example.disasterresponseplatform.data.database.userdata

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.disasterresponseplatform.data.database.DatabaseInfo

@Dao
interface UserDataDao {

    @Insert
    suspend fun insertActivation(userData: UserData)

    @Query("SELECT ${UserDataCols.email} FROM ${DatabaseInfo.USER_DATA}  WHERE ${UserDataCols.username} = :username")
    fun getEmail(username: String): String

}