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
import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.databinding.ResourceItemBinding

class ResourceAdapter(private val resourceList: List<Resource>?): RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder>() {

    inner class ResourceViewHolder(val binding: ResourceItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ResourceItemBinding = DataBindingUtil.inflate(inflater, R.layout.resource_item, parent, false)
        return ResourceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        adjustItems(holder,position)
    }

    /**
     * It adjusts every item on recyclerView (since it's an adapter of recyclerView)
     */
    @SuppressLint("SetTextI18n")
    private fun adjustItems(holder: ResourceViewHolder, position: Int){
        val currentResource = resourceList?.get(position) // to get current item in resourceList
        val hb = holder.binding // to bind xml items


        hb.tvType.text = currentResource?.type.toString()

        hb.tvDate.text = currentResource?.creationTime
        hb.tvLocation.text = "x: ${currentResource?.coordinateX.toString()}, y: ${currentResource?.coordinateY.toString()}"
        hb.tvQuantity.text = currentResource?.quantity.toString()
        hb.tvCreator.text = currentResource?.creatorName

        // for make them clickable
        holder.itemView.setOnClickListener {view ->
            view.isActivated = true // make it active, then its background color will change thanks to selector_item_background
            liveDataResource.postValue(currentResource!!)
            android.os.Handler(Looper.getMainLooper()).postDelayed({ // it's a delay block
                view.isActivated = false // make it false to set its original color again
            }, 50)
        }
    }

    private val liveDataResource = MutableLiveData<Resource>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveIntent(): LiveData<Resource> = liveDataResource

    override fun getItemCount(): Int {
        return if (resourceList.isNullOrEmpty()) 0 else resourceList.size
    }

}