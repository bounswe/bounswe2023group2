package com.example.disasterresponseplatform.data.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.data.database.need.NeedDao
import com.example.disasterresponseplatform.data.database.userdata.UserData
import com.example.disasterresponseplatform.data.database.userdata.UserDataDao
import com.example.disasterresponseplatform.data.database.action.Action
import com.example.disasterresponseplatform.data.database.action.ActionDao
import com.example.disasterresponseplatform.data.database.emergency.Emergency
import com.example.disasterresponseplatform.data.database.emergency.EmergencyDao
import com.example.disasterresponseplatform.data.database.event.Event
import com.example.disasterresponseplatform.data.database.event.EventDao
import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.data.database.resource.ResourceDao

@Database(entities = [Need::class, UserData::class, Action::class, Event::class, Emergency::class, Resource::class], version = DatabaseInfo.DATABASE_VERSION, exportSchema = false)
abstract class DarpDB: RoomDatabase() {
    abstract val needDao: NeedDao
    abstract val userDataDao: UserDataDao
    abstract val actionDao: ActionDao
    abstract val eventDao: EventDao
    abstract val emergencyDao: EmergencyDao
    abstract val resourceDao: ResourceDao

    // it's static object in this way you can call getInstance method without any initialization
    companion object{
        @Volatile //this makes this field visible to other threads
        private var firstInstance: DarpDB? = null

        fun getInstance(context: Context): DarpDB {
            synchronized(this) {
                var instance = firstInstance
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DarpDB::class.java, DatabaseInfo.DATABASE
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration() // when migrate a new version delete the existing db
                        .build()
                    firstInstance = instance
                }
                return instance
            }
        }
    }

}