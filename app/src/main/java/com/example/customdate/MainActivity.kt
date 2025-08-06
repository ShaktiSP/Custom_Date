package com.example.customdate

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.YearMonth

class MainActivity : AppCompatActivity() {
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var tvMonthYear: TextView
    private lateinit var btnPrevious: ImageView
    private lateinit var btnNext: ImageView
    private lateinit var adapter: DayAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    private var currentMonth: YearMonth = YearMonth.now()
    private val selectedDates = mutableListOf<LocalDate>()

    @RequiresApi(Build.VERSION_CODES.O)
    private val today = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        tvMonthYear = findViewById(R.id.tvMonthYear)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)

        calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)

        btnPrevious.setOnClickListener {
            currentMonth = currentMonth.minusMonths(1)
            loadMonth()
        }

        btnNext.setOnClickListener {
            currentMonth = currentMonth.plusMonths(1)
            loadMonth()
        }

        loadMonth()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadMonth() {
        val days = generateDaysForMonth(currentMonth)

        // agar current month hai to current date ko select karo
        if (currentMonth == YearMonth.from(today) && !selectedDates.contains(today)) {
            selectedDates.add(today)
        }

        tvMonthYear.text = "${
            currentMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }
        } ${currentMonth.year}"

        // agar current month hai to previous button ko hide karo
        btnPrevious.visibility =
            if (currentMonth.isAfter(YearMonth.from(today))) View.VISIBLE else View.INVISIBLE

        adapter = DayAdapter(days, selectedDates) { date ->
            if (selectedDates.contains(date)) selectedDates.remove(date)
            else selectedDates.add(date)
            adapter.notifyDataSetChanged()
        }

        calendarRecyclerView.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateDaysForMonth(month: YearMonth): List<Any?> {
        val days = mutableListOf<Any?>()
        val weekHeaders = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")
        days.addAll(weekHeaders)

        val firstDay = month.atDay(1)
        val dayOfWeek = firstDay.dayOfWeek.value  // Monday=1 .. Sunday=7
        val dayIndex = if (dayOfWeek == 7) 6 else dayOfWeek - 1 // convert to 0-based index (Mon=0)

        repeat(dayIndex) { days.add(null) }

        var current = firstDay
        val lastDay = month.atEndOfMonth()
        while (!current.isAfter(lastDay)) {
            days.add(current)
            current = current.plusDays(1)
        }
        return days
    }
}
