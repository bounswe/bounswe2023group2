package com.example.disasterresponseplatform.adapter

import android.annotation.SuppressLint
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.models.EventBody
import com.example.disasterresponseplatform.databinding.EventItemBinding

class EventAdapter(private val eventList: List<EventBody.EventRequestBody>?): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(val binding: EventItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: EventItemBinding = DataBindingUtil.inflate(inflater, R.layout.event_item, parent, false)
        return EventViewHolder(binding)
    }


    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        adjustItems(holder,position)
    }

    /**
     * It adjusts every item on recyclerView (since it's an adapter of recyclerView)
     */
    @SuppressLint("SetTextI18n")
    private fun adjustItems(holder: EventViewHolder, position: Int){
        val currentEvent = eventList?.get(position) // to get current item in activityList
        val hb = holder.binding // to bind xml items


        hb.tvType.text = currentEvent?.event_type.toString()
        hb.tvDate.text = currentEvent?.created_time?.substring(0,19)
        hb.tvLocation.text = "x: ${String.format("%.2f", currentEvent?.x).replace(',', '.')}, y: ${String.format("%.2f", currentEvent?.y).replace(',', '.')}"
        val shortDescription = currentEvent?.short_description.toString()
        hb.tvShortNode.text = shortDescription.substring(0, minOf(25,shortDescription.length))
        hb.tvCreator.text = currentEvent?.created_by_user
        hb.tvDownvoteCount.text = currentEvent?.downvote.toString()
        hb.tvUpvoteCount.text = currentEvent?.upvote.toString()

        // for make them clickable
        holder.itemView.setOnClickListener {view ->
            view.isActivated = true // make it active, then its background color will change thanks to selector_item_background
            liveDataEvent.postValue(currentEvent!!)
            android.os.Handler(Looper.getMainLooper()).postDelayed({ // it's a delay block
                view.isActivated = false // make it false to set its original color again
            }, 50)
        }
    }


    private val liveDataEvent = MutableLiveData<EventBody.EventRequestBody>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveIntent(): LiveData<EventBody.EventRequestBody> = liveDataEvent


    override fun getItemCount(): Int {
        return if (eventList.isNullOrEmpty()) 0
        else eventList.size
    }


}