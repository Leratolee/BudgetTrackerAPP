package com.example.budgettracker

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.budgettracker.data.AppDatabase
import com.example.budgettracker.data.Expense
import kotlinx.coroutines.launch
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    private var photoPath: String? = null
    private val categories = listOf(
        "Food","Transport","Entertainment",
        "Health","Clothing","Utilities","Other"
    )

    private val takePicture = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            findViewById<ImageView>(R.id.imgPreview)
                .setImageURI(Uri.fromFile(File(photoPath!!)))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        val spinner = findViewById<Spinner>(R.id.spinnerCategory)
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )

        findViewById<Button>(R.id.btnTakePhoto).setOnClickListener {
            launchCamera()
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveExpense()
        }
    }

    private fun launchCamera() {
        val photoFile = createImageFile()
        photoPath = photoFile.absolutePath
        val uri = FileProvider.getUriForFile(
            this, "${packageName}.provider", photoFile
        )
        takePicture.launch(uri)
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss", Locale.getDefault()
        ).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
    }

    private fun saveExpense() {
        val title = findViewById<EditText>(R.id.etTitle).text.toString().trim()
        val amountStr = findViewById<EditText>(R.id.etAmount).text.toString().trim()
        val category = findViewById<Spinner>(R.id.spinnerCategory)
            .selectedItem.toString()

        if (title.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields",
                Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toDoubleOrNull() ?: run {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show()
            return
        }

        val expense = Expense(
            title = title,
            amount = amount,
            category = category,
            date = System.currentTimeMillis(),
            photoPath = photoPath
        )

        lifecycleScope.launch {
            AppDatabase.getDatabase(this@AddExpenseActivity)
                .expenseDao().insert(expense)
            runOnUiThread {
                Toast.makeText(this@AddExpenseActivity,
                    "Expense saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}