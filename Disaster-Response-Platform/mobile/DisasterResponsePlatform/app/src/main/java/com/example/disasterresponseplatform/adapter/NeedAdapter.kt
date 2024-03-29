package com.example.disasterresponseplatform.adapter

import android.annotation.SuppressLint
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.models.NeedBody
import com.example.disasterresponseplatform.databinding.NeedItemBinding
import com.example.disasterresponseplatform.ui.activity.generalViewModels.UserRoleViewModel
import com.example.disasterresponseplatform.utils.UserRoleUtil


class NeedAdapter(private val needList: List<NeedBody.NeedItem>?, private val userRoleViewModel: UserRoleViewModel, private val fragmentActivity: FragmentActivity): RecyclerView.Adapter<NeedAdapter.NeedViewHolder>() {

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
        hb.tvDate.text = currentNeed?.created_at?.substring(0,19)
        hb.tvLocation.text = "x: ${String.format("%.2f", currentNeed?.x).replace(',', '.')}, y: ${String.format("%.2f", currentNeed?.y).replace(',', '.')}"
        hb.tvQuantity.text = currentNeed?.initialQuantity.toString()
        hb.tvCreator.text = currentNeed?.created_by
        hb.tvSubType.text = currentNeed?.details?.get("subtype").toString()
        hb.tvReliability.text =  String.format("%.2f", currentNeed?.reliability)


        val creator = currentNeed?.created_by
        // user role
        hb.color.background = AppCompatResources.getDrawable(hb.root.context, R.drawable.borderless_button)
        if (creator != null) UserRoleUtil.isCredibleNonBlocking(creator) {
            if (it) hb.color.background = AppCompatResources.getDrawable(hb.root.context, R.drawable.bordered_button)
        }

        // for make them clickable
        holder.itemView.setOnClickListener {view ->
            view.isActivated = true // make it active, then its background color will change thanks to selector_item_background
            liveDataNeed.postValue(currentNeed!!)
            android.os.Handler(Looper.getMainLooper()).postDelayed({ // it's a delay block
                view.isActivated = false // make it false to set its original color again
            }, 50)
        }
    }


    private val liveDataNeed = MutableLiveData<NeedBody.NeedItem>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveIntent(): LiveData<NeedBody.NeedItem> = liveDataNeed


    override fun getItemCount(): Int {
        return if (needList.isNullOrEmpty()) 0
        else needList.size
    }

}