package com.example.disasterresponseplatform.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.ui.activity.event.EventViewModel
import com.example.disasterresponseplatform.ui.activity.need.NeedViewModel
import com.example.disasterresponseplatform.ui.activity.resource.ResourceViewModel

/**
 * This class includes methods that will used common in most of the classes (activity classes i.e)
 */
class GeneralUtil {

    companion object{

        /**
         * It checks whether the device connects to the internet
         */
        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val activeNetwork =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

                return when {
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    // For other device how are able to connect with Ethernet
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                val networkInfo = connectivityManager.activeNetworkInfo
                return networkInfo != null && networkInfo.isConnected
            }
        }

        fun checkLocalChanges(needViewModel: NeedViewModel,resourceViewModel: ResourceViewModel,eventViewModel: EventViewModel,activity: FragmentActivity){
            checkLocalNeeds(needViewModel,activity)
            checkLocalResources(resourceViewModel,activity)
            checkLocalEvents(eventViewModel,activity)
        }


        private val isPosted = MutableLiveData<Boolean>()
        // this is for updating LiveData, it can be observed from where it is called
        // in this way toast should shown in home fragment by observing it
        fun getIsPosted(): LiveData<Boolean> = isPosted

        /**
         * This function initializes timer for waiting isPosted
         * If timer couldn't stop in 10 seconds (enough for posting)
         * It post isPosted as false which means not posted anything to observing it from different activities
         * it @return timer to canceling when there is a posted from where it is called.
         */
        fun getIsPostedTimer(): CountDownTimer {
            //first create an Info dialog for processing, when this is showing a 10 seconds timer starts
            val timer: CountDownTimer = object : CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}

                override fun onFinish() { //when it's finished, (after 10 seconds)
                    Log.i("CardService","Not connected")
                    isPosted.postValue(false)
                }
            }
            return timer
        }

        /**
         * This function checks whether device has needs in local database
         * If there were some local needs, if current username matches with the creator name of that needs,
         * it post these needs into backend and remove them from local database
         */
        private fun checkLocalNeeds(needViewModel: NeedViewModel,activity: FragmentActivity){
            if (needViewModel.getAllNeeds().isNullOrEmpty()){
                Log.i("Check Local Objects","Needs are empty")
            }else{
                if (DiskStorageManager.checkToken()){ // if there is a user logs in
                    val needList = needViewModel.getAllNeeds()
                    if (needList != null) {
                        Log.i("Check Local Objects","Needs are not empty")
                        val needListCopy = needList.toList() // to do this operations without any error because I deleted them
                        for (need in needListCopy){
                            //if current username matches with creator
                            if (DiskStorageManager.checkUsername(need.creatorName)){
                                val needBody = needViewModel.prepareBodyFromLocal(need)
                                Log.i("ResponseInfo", "Local need is sending")
                                needViewModel.postNeedRequest(needBody)
                                needViewModel.getLiveDataNeedID().observe(activity){postedID ->
                                    if (postedID == "-1"){ //  in error cases it returns this
                                        Log.i("Check Local Objects", "Error occurred post Need")
                                    }else{
                                        Log.i("Check Local Objects", "Post Need Successfully")
                                        isPosted.postValue(true)
                                        needViewModel.deleteNeed(need.ID!!) //delete it from local
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        /**
         * This function checks whether device has resources in local database
         * If there were some local resources, if current username matches with the creator name of that resources,
         * it post these resources into backend and remove them from local database
         */
        private fun checkLocalResources(resourceViewModel: ResourceViewModel,activity: FragmentActivity){
            if (resourceViewModel.getAllResources().isNullOrEmpty()){
                Log.i("Check Local Objects","Resources are empty")
            }else{
                if (DiskStorageManager.checkToken()){ // if there is a user logs in
                    val resourceList = resourceViewModel.getAllResources()
                    if (resourceList != null) {
                        Log.i("Check Local Objects","Resources are not empty")
                        val resourceListCopy = resourceList.toList() // to do this operations without any error because I deleted them
                        for (resource in resourceListCopy){
                            //if current username matches with creator
                            if (DiskStorageManager.checkUsername(resource.creatorName)){
                                val resourceBody = resourceViewModel.prepareBodyFromLocal(resource)
                                Log.i("ResponseInfo", "Local resource is sending")
                                resourceViewModel.postResourceRequest(resourceBody)
                                resourceViewModel.getLiveDataResourceID().observe(activity){postedID ->
                                    if (postedID == "-1"){ //  in error cases it returns this
                                        Log.i("Check Local Objects", "Error occurred post Resource")
                                    }else{
                                        Log.i("Check Local Objects", "Post Resource Successfully")
                                        isPosted.postValue(true)
                                        resourceViewModel.deleteResource(resource.ID!!) //delete it from local
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        /**
         * This function checks whether device has events in local database
         * If there were some local events, if current username matches with the creator name of that events,
         * it post these events into backend and remove them from local database
         */
        private fun checkLocalEvents(eventViewModel: EventViewModel,activity: FragmentActivity){
            if (eventViewModel.getAllEvents().isNullOrEmpty()){
                Log.i("Check Local Objects","Events are empty")
            }else{
                if (DiskStorageManager.checkToken()){ // if there is a user logs in
                    val eventList = eventViewModel.getAllEvents()
                    if (eventList != null) {
                        Log.i("Check Local Objects","Events are not empty")
                        val eventListCopy = eventList.toList() // to do this operations without any error because I deleted them
                        for (event in eventListCopy){
                            //if current username matches with creator
                            if (DiskStorageManager.checkUsername(event.creatorName)){
                                val eventBody = eventViewModel.prepareBodyFromLocal(event)
                                Log.i("ResponseInfo", "Local event is sending")
                                eventViewModel.postEvent(eventBody)
                                eventViewModel.getLiveDataEventID().observe(activity){postedID ->
                                    if (postedID == "-1"){ //  in error cases it returns this
                                        Log.i("Check Local Objects", "Error occurred post Event")
                                    }else{
                                        Log.i("Check Local Objects", "Post Event Successfully")
                                        isPosted.postValue(true)
                                        eventViewModel.deleteEvent(event.ID!!) //delete it from local
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}