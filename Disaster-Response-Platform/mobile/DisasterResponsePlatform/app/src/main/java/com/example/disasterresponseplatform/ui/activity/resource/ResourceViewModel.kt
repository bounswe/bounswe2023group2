package com.example.disasterresponseplatform.ui.activity.resource

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.data.repositories.ResourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResourceViewModel @Inject constructor(private val resourceRepository: ResourceRepository): ViewModel() {

    fun insertResource(resource: Resource){
        viewModelScope.launch(Dispatchers.IO) {
            resourceRepository.insertResource(resource)
        }

    }

    fun getLocation(creatorName: String): String? = resourceRepository.getLocation(creatorName)

    fun getAllResources(): List<Resource>? = resourceRepository.getAllResources()

}