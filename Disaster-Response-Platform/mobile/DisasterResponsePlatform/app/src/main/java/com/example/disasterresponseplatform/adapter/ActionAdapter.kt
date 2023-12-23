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
import com.example.disasterresponseplatform.data.models.ActionBody
import com.example.disasterresponseplatform.databinding.ActionItemBinding


class ActionAdapter(private val actionList: List<ActionBody.ActionItem>?): RecyclerView.Adapter<ActionAdapter.ActionViewHolder>() {

    inner class ActionViewHolder(val binding: ActionItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ActionItemBinding = DataBindingUtil.inflate(inflater, R.layout.action_item, parent, false)
        return ActionViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        adjustItems(holder,position)
    }

    /**
     * It adjusts every item on recyclerView (since it's an adapter of recyclerView)
     */
    @SuppressLint("SetTextI18n")
    private fun adjustItems(holder: ActionViewHolder, position: Int){
        val currentAction = actionList?.get(position) // to get current item in activityList
        val hb = holder.binding // to bind xml items


        hb.tvType.text = currentAction?.type.toString()
        hb.tvDate.text = currentAction?.created_at?.substring(0,19)
        hb.tvCreator.text = currentAction?.created_by
        hb.tvDownvoteCount.text = currentAction?.downvote.toString()
        hb.tvUpvoteCount.text = currentAction?.upvote.toString()

        // for make them clickable
        holder.itemView.setOnClickListener {view ->
            view.isActivated = true // make it active, then its background color will change thanks to selector_item_background
            liveDataAction.postValue(currentAction!!)
            android.os.Handler(Looper.getMainLooper()).postDelayed({ // it's a delay block
                view.isActivated = false // make it false to set its original color again
            }, 50)
        }
    }


    private val liveDataAction = MutableLiveData<ActionBody.ActionItem>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveIntent(): LiveData<ActionBody.ActionItem> = liveDataAction


    override fun getItemCount(): Int {
        return if (actionList.isNullOrEmpty()) 0
        else actionList.size
    }

}