package com.example.budgettracker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgettracker.data.AppDatabase
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class CategorySummaryActivity : AppCompatActivity() {

    private val periods = listOf(
        "This Week","This Month","Last 3 Months","This Year"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_summary)

        val spinner = findViewById<Spinner>(R.id.spinnerPeriod)
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            periods
        )

        findViewById<Button>(R.id.btnFilter).setOnClickListener {
            loadSummary()
        }
        loadSummary()
    }

    private fun loadSummary() {
        val spinner = findViewById<Spinner>(R.id.spinnerPeriod)
        val (from, to) = getPeriodRange(spinner.selectedItemPosition)

        lifecycleScope.launch {
            val totals = AppDatabase.getDatabase(this@CategorySummaryActivity)
                .expenseDao().getCategoryTotals(from, to)
            val currency = NumberFormat.getCurrencyInstance()
            runOnUiThread {
                val container = findViewById<LinearLayout>(R.id.listView)
                container.removeAllViews()
                if (totals.isEmpty()) {
                    container.addView(TextView(this@CategorySummaryActivity).apply {
                        text = "No data for this period."
                    })
                    return@runOnUiThread
                }
                totals.forEach { ct ->
                    container.addView(TextView(this@CategorySummaryActivity).apply {
                        text = "${ct.category}: ${currency.format(ct.total)}"
                        textSize = 16f
                        setPadding(8, 12, 8, 12)
                    })
                }
            }
        }
    }

    private fun getPeriodRange(index: Int): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        val to = cal.timeInMillis
        when (index) {
            0 -> cal.add(Calendar.DAY_OF_YEAR, -7)
            1 -> cal.add(Calendar.MONTH, -1)
            2 -> cal.add(Calendar.MONTH, -3)
            3 -> cal.add(Calendar.YEAR, -1)
        }
        return Pair(cal.timeInMillis, to)
    }
}