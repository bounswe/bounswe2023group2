package com.example.disasterresponseplatform.ui.activity.action

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterresponseplatform.data.database.action.Action
import com.example.disasterresponseplatform.data.repositories.ActionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActionViewModel@Inject constructor(private val actionRepository: ActionRepository) : ViewModel() {

    /**
     * It inserts action in a background (IO) thread
     */
    fun insertAction(action: Action){
        viewModelScope.launch(Dispatchers.IO){
            actionRepository.insertAction(action)
        }
    }

    fun getStartLocation(creatorID: String): String{
        return actionRepository.getStartLocation(creatorID)
    }

}