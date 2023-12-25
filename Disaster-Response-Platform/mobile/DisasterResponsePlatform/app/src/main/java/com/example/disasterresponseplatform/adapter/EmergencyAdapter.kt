package com.example.disasterresponseplatform.adapter

import android.annotation.SuppressLint
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.models.EmergencyBody
import com.example.disasterresponseplatform.databinding.EmergencyItemBinding


class EmergencyAdapter(private val emergencyList: List<EmergencyBody.EmergencyItem>?, val userRoleMap: MutableMap<String, String>): RecyclerView.Adapter<EmergencyAdapter.EmergencyViewHolder>() {

    inner class EmergencyViewHolder(val binding: EmergencyItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: EmergencyItemBinding = DataBindingUtil.inflate(inflater, R.layout.emergency_item, parent, false)
        return EmergencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmergencyAdapter.EmergencyViewHolder, position: Int) {
        adjustItems(holder,position)
    }

    /**
     * It adjusts every item on recyclerView (since it's an adapter of recyclerView)
     */
    @SuppressLint("SetTextI18n")
    private fun adjustItems(holder: EmergencyAdapter.EmergencyViewHolder, position: Int){
        val currentEmergency = emergencyList?.get(position) // to get current item in activityList
        val hb = holder.binding // to bind xml items


        hb.tvType.text = currentEmergency?.type.toString()
        hb.tvDate.text = currentEmergency?.created_at.toString()
     //   hb.tvLocation.text = "x: ${String.format("%.2f", currentEmergency?.x).replace(',', '.')}, y: ${String.format("%.2f", currentEmergency?.y).replace(',', '.')}"
        hb.tvLocation.text = currentEmergency?.location.toString()
        // user role
        val creator = currentEmergency?.created_by
        if (creator in userRoleMap.keys && userRoleMap[creator] == "CREDIBLE") {
            hb.color.background = AppCompatResources.getDrawable(hb.root.context, R.drawable.bordered_button)
        } else {
            hb.color.background = AppCompatResources.getDrawable(hb.root.context, R.drawable.borderless_button)
        }

        // for make them clickable
        holder.itemView.setOnClickListener {view ->
            view.isActivated = true // make it active, then its background color will change thanks to selector_item_background
            liveDataEmergency.postValue(currentEmergency!!)
            android.os.Handler(Looper.getMainLooper()).postDelayed({ // it's a delay block
                view.isActivated = false // make it false to set its original color again
            }, 50)
        }
    }


    private val liveDataEmergency = MutableLiveData<EmergencyBody.EmergencyItem>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveIntent(): LiveData<EmergencyBody.EmergencyItem> = liveDataEmergency


    override fun getItemCount(): Int {
        return if (emergencyList.isNullOrEmpty()) 0
        else emergencyList.size
    }

}