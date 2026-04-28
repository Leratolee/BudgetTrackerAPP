package com.example.budgettracker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.budgettracker.data.GoalSettings

class GoalsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        val seekBarMin = findViewById<SeekBar>(R.id.seekBarMin)
        val seekBarMax = findViewById<SeekBar>(R.id.seekBarMax)
        val tvMin = findViewById<TextView>(R.id.tvMinLabel)
        val tvMax = findViewById<TextView>(R.id.tvMaxLabel)

        seekBarMin.progress = GoalSettings.getMinGoal(this).toInt()
        seekBarMax.progress = GoalSettings.getMaxGoal(this).toInt()

        seekBarMin.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar, progress: Int, fromUser: Boolean) {
                tvMin.text = "Min Goal: R$progress"
            }
            override fun onStartTrackingTouch(sb: SeekBar) {}
            override fun onStopTrackingTouch(sb: SeekBar) {}
        })

        seekBarMax.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar, progress: Int, fromUser: Boolean) {
                tvMax.text = "Max Goal: R$progress"
            }
            override fun onStartTrackingTouch(sb: SeekBar) {}
            override fun onStopTrackingTouch(sb: SeekBar) {}
        })

        findViewById<Button>(R.id.btnSaveGoals).setOnClickListener {
            val min = seekBarMin.progress.toFloat()
            val max = seekBarMax.progress.toFloat()
            if (min > max) {
                Toast.makeText(this,
                    "Min cannot be greater than Max",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            GoalSettings.saveGoals(this, min, max)
            Toast.makeText(this, "Goals saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}