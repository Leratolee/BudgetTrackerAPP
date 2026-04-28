package com.example.budgettracker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgettracker.data.AppDatabase
import com.example.budgettracker.data.Expense
import kotlinx.coroutines.launch
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ExpenseListActivity : AppCompatActivity() {

    private val periods = listOf(
        "This Week","This Month","Last 3 Months","This Year"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_list)

        val spinner = findViewById<Spinner>(R.id.spinnerPeriod)
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            periods
        )

        findViewById<Button>(R.id.btnFilter).setOnClickListener {
            loadExpenses()
        }
        loadExpenses()
    }

    private fun loadExpenses() {
        val spinner = findViewById<Spinner>(R.id.spinnerPeriod)
        val (from, to) = getPeriodRange(spinner.selectedItemPosition)

        lifecycleScope.launch {
            val expenses = AppDatabase.getDatabase(this@ExpenseListActivity)
                .expenseDao().getByPeriod(from, to)
            runOnUiThread { displayExpenses(expenses) }
        }
    }

    private fun displayExpenses(list: List<Expense>) {
        val container = findViewById<LinearLayout>(R.id.listView)
        val tvEmpty = findViewById<TextView>(R.id.tvEmpty)
        container.removeAllViews()

        if (list.isEmpty()) {
            tvEmpty.visibility = android.view.View.VISIBLE
            return
        }
        tvEmpty.visibility = android.view.View.GONE

        val currency = NumberFormat.getCurrencyInstance()
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        list.forEach { expense ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(8, 16, 8, 16)
            }
            val info = TextView(this).apply {
                text = "${expense.title} — ${currency.format(expense.amount)}\n" +
                        "${expense.category} | ${dateFormat.format(Date(expense.date))}"
                layoutParams = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                )
            }
            row.addView(info)

            if (expense.photoPath != null && File(expense.photoPath).exists()) {
                val btn = Button(this).apply {
                    text = "View Photo"
                    setOnClickListener {
                        val uri = Uri.fromFile(File(expense.photoPath))
                        startActivity(Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, "image/jpeg")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        })
                    }
                }
                row.addView(btn)
            }
            container.addView(row)
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