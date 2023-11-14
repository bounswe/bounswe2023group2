package com.example.disasterresponseplatform.ui.activity.need

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.data.repositories.NeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NeedViewModel@Inject constructor(private val needRepository: NeedRepository) : ViewModel() {

    /**
     * It inserts need in a background (IO) thread
     */
    fun insertNeed(need: Need){
        viewModelScope.launch(Dispatchers.IO){
            needRepository.insertNeed(need)
        }
    }

    fun updateNeed(need: Need){
        viewModelScope.launch(Dispatchers.IO){
            needRepository.updateNeed(need)
        }
    }

    fun getX(creatorID: String): Double?{
        return needRepository.getX(creatorID)
    }

    fun getY(creatorID: String): Double?{
        return needRepository.getY(creatorID)
    }

    fun getAllNeeds(): List<Need>? = needRepository.getAllNeeds()

}