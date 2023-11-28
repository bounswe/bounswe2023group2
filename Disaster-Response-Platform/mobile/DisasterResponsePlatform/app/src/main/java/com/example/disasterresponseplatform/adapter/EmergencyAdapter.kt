package com.example.disasterresponseplatform.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.databinding.EmergencyItemBinding


class EmergencyAdapter(private val emergencyList: List<String>): RecyclerView.Adapter<EmergencyAdapter.EmergencyViewHolder>() {

    inner class EmergencyViewHolder(val binding: EmergencyItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: EmergencyItemBinding = DataBindingUtil.inflate(inflater, R.layout.emergency_item, parent, false)
        return EmergencyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return emergencyList.size
    }

    override fun onBindViewHolder(holder: EmergencyViewHolder, position: Int) {
        val currentEmergency = emergencyList[position] // to get current item in activityList
        val hb = holder.binding // to bind xml items


    }

}