package com.example.disasterresponseplatform.data.repositories

import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.data.database.resource.ResourceDao
import javax.inject.Inject

class ResourceRepository @Inject constructor(private val resourceDao: ResourceDao) {

    suspend fun insertResource(resource: Resource){
        resourceDao.insertResource(resource)
    }

    fun getX(creatorID: String): Double?{
        return resourceDao.getX(creatorID)
    }

    fun getY(creatorID: String): Double?{
        return resourceDao.getY(creatorID)
    }

    fun getAllResources(): List<Resource>? = resourceDao.getAllResources()
}
