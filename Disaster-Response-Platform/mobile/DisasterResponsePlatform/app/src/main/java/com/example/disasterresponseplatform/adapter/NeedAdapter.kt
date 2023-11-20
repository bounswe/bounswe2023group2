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
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.databinding.NeedItemBinding


class NeedAdapter(private val needList: List<Need>?): RecyclerView.Adapter<NeedAdapter.NeedViewHolder>() {

    inner class NeedViewHolder(val binding: NeedItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NeedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: NeedItemBinding = DataBindingUtil.inflate(inflater, R.layout.need_item, parent, false)
        return NeedViewHolder(binding)
    }


    override fun onBindViewHolder(holder: NeedViewHolder, position: Int) {
        adjustItems(holder,position)
    }

    /**
     * It adjusts every item on recyclerView (since it's an adapter of recyclerView)
     */
    @SuppressLint("SetTextI18n")
    private fun adjustItems(holder: NeedViewHolder, position: Int){
        val currentNeed = needList?.get(position) // to get current item in activityList
        val hb = holder.binding // to bind xml items


        hb.tvType.text = currentNeed?.type.toString()
        hb.tvDate.text = currentNeed?.creationTime
        hb.tvLocation.text = "x: ${String.format("%.2f", currentNeed?.coordinateX).replace(',', '.')}, y: ${String.format("%.2f", currentNeed?.coordinateY).replace(',', '.')}"
        hb.tvQuantity.text = currentNeed?.quantity.toString()
        hb.tvCreator.text = currentNeed?.creatorName

        // for make them clickable
        holder.itemView.setOnClickListener {view ->
            view.isActivated = true // make it active, then its background color will change thanks to selector_item_background
            liveDataNeed.postValue(currentNeed!!)
            android.os.Handler(Looper.getMainLooper()).postDelayed({ // it's a delay block
                view.isActivated = false // make it false to set its original color again
            }, 50)
        }
    }


    private val liveDataNeed = MutableLiveData<Need>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveIntent(): LiveData<Need> = liveDataNeed


    override fun getItemCount(): Int {
        return if (needList.isNullOrEmpty()) 0
        else needList.size
    }

}