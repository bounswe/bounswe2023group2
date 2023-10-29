package com.example.disasterresponseplatform.ui.activity.userdata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterresponseplatform.data.database.userdata.UserData
import com.example.disasterresponseplatform.data.repositories.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(private val userDataRepository: UserDataRepository) : ViewModel() {
    fun insertUserData(userData: UserData){
        viewModelScope.launch(Dispatchers.IO) {
            userDataRepository.insertUserData(userData)
        }
    }

    fun getEmail(username: String): String{
        return userDataRepository.getEmail(username)
    }
}