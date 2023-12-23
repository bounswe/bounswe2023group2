package com.example.disasterresponseplatform.adapter

import android.graphics.Color
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.enums.ActivityEnum
import com.example.disasterresponseplatform.data.models.DummyActivity
import com.example.disasterresponseplatform.data.enums.PredefinedTypes
import com.example.disasterresponseplatform.databinding.ActivityItemBinding

class ActivityAdapter(private val activityList: MutableList<DummyActivity>): RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    inner class ActivityViewHolder(val binding: ActivityItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ActivityItemBinding = DataBindingUtil.inflate(inflater, R.layout.activity_item, parent, false)
        return ActivityViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        adjustItems(holder,position)
     }

    /**
     * It adjusts every item on recyclerView (since it's an adapter of recyclerView)
     */
    private fun adjustItems(holder: ActivityViewHolder, position: Int){
        val currentActivity = activityList[position] // to get current item in activityList
        val hb = holder.binding // to bind xml items

        hb.tvActivity.text = when(currentActivity.activityType){
            ActivityEnum.Action -> "Activity"
            ActivityEnum.Emergency -> "Emergency"
            ActivityEnum.Event -> "Event"
            ActivityEnum.Need -> "Need"
            ActivityEnum.Resource -> "Resource"
        }

        hb.tvType.text = when(currentActivity.predefinedTypes){
            PredefinedTypes.Clothes -> "Clothes"
            PredefinedTypes.Human -> "Human"
            PredefinedTypes.Food -> "Food"
            PredefinedTypes.Collapse -> "Food"
            PredefinedTypes.Debris -> "Debris"
        }

        hb.tvDate.text = currentActivity.date
        hb.tvLocation.text = currentActivity.location
        hb.tvReliabilityScale.text = currentActivity.reliabilityScale.toString()

        if (currentActivity.reliabilityScale < 0.3) {
            hb.tvReliabilityScale.setTextColor(Color.parseColor("#A80C0C"))
        } else if (currentActivity.reliabilityScale > 0.7) {
            hb.tvReliabilityScale.setTextColor(Color.parseColor("#067A47"))
        }

        // for make them clickable
        holder.itemView.setOnClickListener {view ->
            view.isActivated = true // make it active, then its background color will change thanks to selector_item_background
            liveDataActivity.postValue(currentActivity)
            android.os.Handler(Looper.getMainLooper()).postDelayed({ // it's a delay block
                view.isActivated = false // make it false to set its original color again
            }, 50)
        }
    }


    private val liveDataActivity = MutableLiveData<DummyActivity>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveIntent(): LiveData<DummyActivity> = liveDataActivity


    override fun getItemCount(): Int {
        return activityList.size
    }

}