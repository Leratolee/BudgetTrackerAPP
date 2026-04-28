package com.example.budgettracker.data



import android.content.Context

object GoalSettings {
    private const val PREFS = "budget_prefs"
    private const val KEY_MIN = "goal_min"
    private const val KEY_MAX = "goal_max"

    fun saveGoals(context: Context, min: Float, max: Float) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putFloat(KEY_MIN, min)
            .putFloat(KEY_MAX, max)
            .apply()
    }

    fun getMinGoal(context: Context): Float =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getFloat(KEY_MIN, 0f)

    fun getMaxGoal(context: Context): Float =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getFloat(KEY_MAX, 10000f)
}