package com.example.disasterresponseplatform.ui.activity.report

// ReportAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.disasterresponseplatform.R

class ReportAdapter(private val reportOptions: List<String>, private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reportCellText: TextView = itemView.findViewById(R.id.reportCellText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reportOption = reportOptions[position]
        holder.reportCellText.text = reportOption

        // Set click listener for the item
        holder.itemView.setOnClickListener {
            onItemClick.invoke(reportOption)
        }
    }

    override fun getItemCount(): Int {
        return reportOptions.size
    }
}
