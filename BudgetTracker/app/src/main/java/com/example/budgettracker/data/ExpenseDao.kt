package com.example.budgettracker.data

import androidx.room.*

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insert(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT * FROM expenses WHERE date BETWEEN :from AND :to ORDER BY date DESC")
    suspend fun getByPeriod(from: Long, to: Long): List<Expense>

    @Query("""
        SELECT category, SUM(amount) as total 
        FROM expenses 
        WHERE date BETWEEN :from AND :to 
        GROUP BY category
    """)
    suspend fun getCategoryTotals(from: Long, to: Long): List<CategoryTotal>
}

data class CategoryTotal(
    val category: String,
    val total: Double
)