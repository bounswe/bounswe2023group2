package com.example.disasterresponseplatform.ui.activity.emergency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.disasterresponseplatform.data.database.DatabaseInfo
import com.example.disasterresponseplatform.data.database.emergency.Emergency
import com.example.disasterresponseplatform.data.repositories.EmergencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmergencyViewModel@Inject constructor(private val emergencyRepository: EmergencyRepository) : ViewModel() {

    fun insertEmergency(emergency: Emergency){
        viewModelScope.launch(Dispatchers.IO){
            emergencyRepository.insertEmergency(emergency)
        }
    }

    fun getAllEmergencies(): List<Emergency>? = emergencyRepository.getAllEmergencies()

    suspend fun deleteAllEmergencies(){
        viewModelScope.launch(Dispatchers.IO){
            emergencyRepository.deleteAllEmergencies()
        }
    }

}