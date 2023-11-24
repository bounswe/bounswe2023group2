package com.example.disasterresponseplatform.ui.map

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.ui.activity.need.NeedItemFragment
import com.example.disasterresponseplatform.ui.activity.need.NeedViewModel
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow

class BubbleInfoView(mapView: MapView?, val parentFragmentManager: FragmentManager,
                     val needViewModel: NeedViewModel, val needItem: Need) : MarkerInfoWindow(R.layout.map_bubble, mapView) {

    override fun onOpen(item: Any?) {
        super.onOpen(item)
        val button = mView.findViewById<View>(R.id.more)
        button.setOnClickListener {
            addFragment(NeedItemFragment(needViewModel, needItem), "NeedItemFragment")
        }
    }

    private fun addFragment(fragment: Fragment, fragmentName: String) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(fragmentName)
        ft.commit()
    }
}