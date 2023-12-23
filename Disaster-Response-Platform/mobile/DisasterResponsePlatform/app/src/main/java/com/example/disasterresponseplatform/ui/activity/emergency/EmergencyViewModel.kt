package com.example.disasterresponseplatform.ui.activity.emergency

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterresponseplatform.data.database.emergency.Emergency
import com.example.disasterresponseplatform.data.models.EmergencyBody
import com.example.disasterresponseplatform.data.repositories.EmergencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmergencyViewModel@Inject constructor(private val emergencyRepository: EmergencyRepository) : ViewModel() {

    /**
     * It inserts emergency in a background (IO) thread
     */
    fun insertEmergency(emergency: Emergency){
        viewModelScope.launch(Dispatchers.IO){
            emergencyRepository.insertEmergency(emergency)
        }
    }


    private val liveDataIsDeleted = MutableLiveData<Boolean>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataIsDeleted(): LiveData<Boolean> = liveDataIsDeleted

    fun deleteEmergency(ID: String) {
        // to do
    }

    private val liveDataResponse = MutableLiveData<EmergencyBody.EmergencyResponse>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataResponse(): LiveData<EmergencyBody.EmergencyResponse> = liveDataResponse

    private val liveDataEmergencyID = MutableLiveData<String>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataEmergencyID(): LiveData<String> = liveDataEmergencyID

    fun getAllEmergencies(): List<Emergency>? = emergencyRepository.getAllEmergencies()

    suspend fun deleteAllEmergencies(){
        viewModelScope.launch(Dispatchers.IO){
            emergencyRepository.deleteAllEmergencies()
        }
    }
}