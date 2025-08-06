package com.example.customdate

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DayAdapter(
    private val days: List<Any?>,
    private val selectedDates: List<LocalDate>,
    private val onDateClick: (LocalDate) -> Unit
) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDay: TextView = itemView.findViewById(R.id.tvDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.date_item, parent, false)
        return DayViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val item = days[position]
        when (item) {
            is String -> { // week header
                holder.tvDay.text = item
                holder.tvDay.setBackgroundColor(Color.TRANSPARENT)
                holder.tvDay.isSelected = false
                holder.tvDay.setOnClickListener(null)
                holder.tvDay.setTextColor(Color.BLACK)
            }
            is LocalDate -> { // normal date
                holder.tvDay.text = item.dayOfMonth.toString()
                holder.tvDay.isSelected = selectedDates.contains(item)
                holder.tvDay.setOnClickListener { onDateClick(item) }
                holder.tvDay.setTextColor(Color.BLACK)
            }
            else -> { // null space
                holder.tvDay.text = ""
                holder.tvDay.setBackgroundColor(Color.TRANSPARENT)
                holder.tvDay.isSelected = false
                holder.tvDay.setOnClickListener(null)
            }
        }
    }

    override fun getItemCount(): Int = days.size
}