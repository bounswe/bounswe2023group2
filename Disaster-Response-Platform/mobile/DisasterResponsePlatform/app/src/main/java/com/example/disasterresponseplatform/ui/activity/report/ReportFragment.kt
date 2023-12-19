package com.example.disasterresponseplatform.ui.activity.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.disasterresponseplatform.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class ReportBottomSheetFragment(val id: String, val type: String) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }
    private var reportViewModel = ReportViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
        users = "users"
        needs = "needs"
        resources = "resources"
        actions = "actions"
        events = "events"
         */
        val reportOptions : List<String>
        if (type != "users") {
             reportOptions = listOf(
                "Inappropriate Content",
                "Spam",
                "Bullying",
                "Fake News",
                "Hate Speech"
            )
        } else {
            reportOptions = listOf(
                "Fake User",
                "Misleading Characters",
                "Bullying",
                "Fake News",
                "Hate Speech"
            )
        }
        val reportList = view.findViewById<RecyclerView>(R.id.reportList)

        // Set up the adapter with an item click listener
        val reportAdapter = ReportAdapter(reportOptions) { selectedItem ->
            reportViewModel.sendReport(id, type,selectedItem)
        }

        reportList.layoutManager = LinearLayoutManager(requireContext())
        reportList.adapter = reportAdapter

        reportViewModel.reportValidation.observe(viewLifecycleOwner, Observer { message ->
            Log.i("yupp",message)
            showToast(message)
            dismiss()
            })
    }


    private fun showToast(message: String) {

        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun sendReport() {

    }
}

