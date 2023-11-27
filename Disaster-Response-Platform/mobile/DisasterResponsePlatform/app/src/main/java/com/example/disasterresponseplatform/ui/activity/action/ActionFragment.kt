package com.example.disasterresponseplatform.ui.activity.action

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.databinding.FragmentActionBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class ActionFragment : Fragment() {

    private lateinit var binding: FragmentActionBinding
    private val addActionFragment = AddActionFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrangeView()

        binding.btAddAction.setOnClickListener {
            addFragment(addActionFragment)
        }
    }

    private fun arrangeView(){
        binding.btFilter.setOnClickListener {
            showFilterDialog()
        }

        binding.btAddAction.setOnClickListener {
            addFragment(addActionFragment)
        }

        val fab: ExtendedFloatingActionButton = binding.btAddAction

        binding.recyclerViewActions.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // if the recycler view is scrolled
                // above shrink the FAB
                if (dy > 30 && fab.isExtended) {
                    fab.shrink()
                }

                // if the recycler view is scrolled
                // above extend the FAB
                if (dy < -30 && !fab.isExtended) {
                    fab.extend()
                }

                // of the recycler view is at the first
                // item always extend the FAB
                if (!recyclerView.canScrollVertically(-1)) {
                    fab.extend()
                }
            }
        })

        //arrangeSearchView()
        //arrangeRecyclerView()
        //sendRequest()
    }


    /**
     * Arrange filter and sort
     */
    private fun showFilterDialog(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.sort_and_filter)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()
    }

    private fun addFragment(fragment: Fragment) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }
}
